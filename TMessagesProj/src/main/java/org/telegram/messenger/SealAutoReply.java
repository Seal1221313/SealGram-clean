package org.telegram.messenger;

import android.content.Context;
import android.content.SharedPreferences;

import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;

public class SealAutoReply implements NotificationCenter.NotificationCenterDelegate {

    public static final String DEFAULT_TEXT = "Зараз мене немає на зв'язку. Відповім, щойно зможу.";
    public static final int[] DELAYS = {15, 30, 60, 180, 360, 720, 1440};
    public static final String[] DELAY_NAMES = {"15 хвилин", "30 хвилин", "1 година", "3 години", "6 годин", "12 годин", "24 години"};

    private static final SealAutoReply INSTANCE = new SealAutoReply();
    private static final boolean[] attached = new boolean[UserConfig.MAX_ACCOUNT_COUNT];
    private static volatile long lastTouch;
    private static volatile long lastSaved;

    private static SharedPreferences prefs() {
        return ApplicationLoader.applicationContext.getSharedPreferences("sealgram", Context.MODE_PRIVATE);
    }

    public static synchronized void attach(int account) {
        if (account < 0 || account >= attached.length || attached[account]) return;
        attached[account] = true;
        NotificationCenter.getInstance(account).addObserver(INSTANCE, NotificationCenter.didReceiveNewMessages);
    }

    public static void touch() {
        long now = System.currentTimeMillis();
        lastTouch = now;
        if (now - lastSaved > 15000) {
            lastSaved = now;
            prefs().edit().putLong("ar_last_active", now).apply();
        }
    }

    public static void touchNow() {
        long now = System.currentTimeMillis();
        lastTouch = now;
        lastSaved = now;
        prefs().edit().putLong("ar_last_active", now).apply();
    }

    public static long lastActive() {
        return Math.max(lastTouch, prefs().getLong("ar_last_active", 0));
    }

    public static boolean enabled() {
        return prefs().getBoolean("ar_enabled", false);
    }

    public static void setEnabled(boolean v) {
        prefs().edit().putBoolean("ar_enabled", v).apply();
    }

    public static String text() {
        String saved = prefs().getString("ar_text", null);
        return saved != null ? saved : SealGramI18n.t("AutoReplyDefault");
    }

    public static void setText(String t) {
        prefs().edit().putString("ar_text", t).apply();
    }

    public static int delayMinutes() {
        return prefs().getInt("ar_delay", 60);
    }

    public static void setDelayMinutes(int m) {
        prefs().edit().putInt("ar_delay", m).apply();
    }

    public static String delayName() {
        int m = delayMinutes();
        for (int i = 0; i < DELAYS.length; i++) if (DELAYS[i] == m) return SealGramI18n.autoReplyDelayNames()[i];
        return m + " " + SealGramI18n.t("HubMinutes");
    }

    public static boolean skipContacts() {
        return prefs().getBoolean("ar_skip_contacts", false);
    }

    public static void setSkipContacts(boolean v) {
        prefs().edit().putBoolean("ar_skip_contacts", v).apply();
    }

    public static ArrayList<Long> excluded() {
        ArrayList<Long> r = new ArrayList<>();
        String s = prefs().getString("ar_excluded", "");
        if (s == null || s.isEmpty()) return r;
        for (String p : s.split(",")) {
            try {
                r.add(Long.parseLong(p.trim()));
            } catch (Exception ignore) {
            }
        }
        return r;
    }

    private static void saveExcluded(ArrayList<Long> list) {
        StringBuilder sb = new StringBuilder();
        for (long id : list) {
            if (sb.length() > 0) sb.append(',');
            sb.append(id);
        }
        prefs().edit().putString("ar_excluded", sb.toString()).apply();
    }

    public static void addExcluded(long id) {
        ArrayList<Long> l = excluded();
        if (!l.contains(id)) {
            l.add(id);
            saveExcluded(l);
        }
    }

    public static void removeExcluded(long id) {
        ArrayList<Long> l = excluded();
        l.remove(Long.valueOf(id));
        saveExcluded(l);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void didReceivedNotification(int id, int account, Object... args) {
        if (id != NotificationCenter.didReceiveNewMessages || !enabled() || args == null || args.length < 3) return;
        try {
            long dialogId = (Long) args[0];
            ArrayList<MessageObject> messages = (ArrayList<MessageObject>) args[1];
            boolean scheduled = (Boolean) args[2];
            if (scheduled || dialogId <= 0 || messages == null || messages.isEmpty()) return;
            if (!ApplicationLoader.mainInterfacePaused) return;
            long now = System.currentTimeMillis();
            long active = lastActive();
            if (active <= 0 || now - active < delayMinutes() * 60000L) return;
            boolean fresh = false;
            for (MessageObject m : messages) {
                if (m == null || m.messageOwner == null || m.isOut() || m.messageOwner.action != null) continue;
                if (m.messageOwner.date * 1000L >= active - 60000L) {
                    fresh = true;
                    break;
                }
            }
            if (!fresh) return;
            if (dialogId == 777000L || excluded().contains(dialogId)) return;
            TLRPC.User user = MessagesController.getInstance(account).getUser(dialogId);
            if (user == null || user.bot || user.self || user.deleted || user.support) return;
            if (skipContacts() && user.contact) return;
            SharedPreferences p = prefs();
            String stampKey = "ar_a_" + account + "_" + dialogId;
            if (p.getLong(stampKey, 0) == active) return;
            String timeKey = "ar_t_" + account + "_" + dialogId;
            if (now - p.getLong(timeKey, 0) < 600000L) return;
            p.edit().putLong(stampKey, active).putLong(timeKey, now).apply();
            SendMessagesHelper.getInstance(account).sendMessage(SendMessagesHelper.SendMessageParams.of(text(), dialogId));
        } catch (Throwable t) {
            FileLog.e(t);
        }
    }
}
