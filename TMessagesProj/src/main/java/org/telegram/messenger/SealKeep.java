package org.telegram.messenger;

import android.content.SharedPreferences;
import android.text.TextUtils;
import androidx.collection.LongSparseArray;

import org.telegram.SQLite.SQLiteCursor;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public final class SealKeep {

    private static final int CAP = 4000;
    private static final int BOGUS_ID = Integer.MIN_VALUE + 7;
    private static final long DAY = 24L * 60 * 60 * 1000;
    private static LinkedHashMap<String, Long> kept;
    private static volatile boolean any;
    private static volatile long lastCleanup;

    private SealKeep() {
    }

    public static boolean enabled() {
        return SealGram.pref("keep_deleted", false);
    }

    public static int days() {
        int d = SealGram.intPref("keep_days", 7);
        return d < 1 ? 7 : d;
    }

    private static SharedPreferences store() {
        return ApplicationLoader.applicationContext.getSharedPreferences("sealgram_kept", 0);
    }

    private static synchronized void load() {
        if (kept != null) return;
        kept = new LinkedHashMap<>();
        try {
            String s = store().getString("items", "");
            if (!TextUtils.isEmpty(s)) {
                for (String p : s.split(",")) {
                    int i = p.lastIndexOf(':');
                    if (i <= 0) continue;
                    try {
                        kept.put(p.substring(0, i), Long.parseLong(p.substring(i + 1)));
                    } catch (Throwable ignore) {
                    }
                }
            }
        } catch (Throwable ignore) {
        }
        any = !kept.isEmpty();
    }

    private static void persist() {
        StringBuilder sb = new StringBuilder();
        for (java.util.Map.Entry<String, Long> e : kept.entrySet()) {
            if (sb.length() > 0) sb.append(',');
            sb.append(e.getKey()).append(':').append(e.getValue());
        }
        any = !kept.isEmpty();
        try {
            store().edit().putString("items", sb.toString()).apply();
        } catch (Throwable ignore) {
        }
    }

    public static int count() {
        load();
        synchronized (SealKeep.class) {
            return kept.size();
        }
    }

    public static boolean isKept(MessageObject mo) {
        try {
            if (mo == null) return false;
            load();
            if (!any) return false;
            synchronized (SealKeep.class) {
                return kept.containsKey(mo.currentAccount + "_" + mo.getDialogId() + "_" + mo.getId());
            }
        } catch (Throwable t) {
            return false;
        }
    }

    public static void prepareReply(SendMessagesHelper.SendMessageParams p) {
        try {
            MessageObject r = p == null ? null : p.replyToMsg;
            if (r == null || r.messageOwner == null || !isKept(r)) return;
            CharSequence text = r.messageText;
            boolean hasBody = !TextUtils.isEmpty(p.message) || !TextUtils.isEmpty(p.caption) || p.photo != null;
            if (hasBody && !TextUtils.isEmpty(text)) {
                String name = "";
                if (!DialogObject.isUserDialog(p.peer)) {
                    long from = r.getFromChatId();
                    if (from > 0) {
                        TLRPC.User u = MessagesController.getInstance(r.currentAccount).getUser(from);
                        if (u != null) name = ContactsController.formatName(u.first_name, u.last_name) + "\n";
                    } else if (from < 0) {
                        TLRPC.Chat c = MessagesController.getInstance(r.currentAccount).getChat(-from);
                        if (c != null && c.title != null) name = c.title + "\n";
                    }
                }
                String flat = text.toString().replace('\n', ' ').trim();
                if (flat.length() > 20) flat = flat.substring(0, 20) + "…";
                String prefix = name + "> " + flat;
                int shift = 0;
                if (!TextUtils.isEmpty(p.message)) {
                    p.message = prefix + "\n\n" + p.message;
                    shift = prefix.length() + 2;
                } else {
                    boolean had = !TextUtils.isEmpty(p.caption);
                    p.caption = had ? prefix + "\n\n" + p.caption : prefix;
                    shift = had ? prefix.length() + 2 : 0;
                }
                if (shift > 0 && p.entities != null) {
                    for (int i = 0; i < p.entities.size(); i++) p.entities.get(i).offset += shift;
                }
            }
            p.replyToMsg = null;
            p.replyQuote = null;
        } catch (Throwable ignore) {
        }
    }

    private static synchronized void remember(ArrayList<String> keys) {
        load();
        long now = System.currentTimeMillis();
        for (String k : keys) kept.put(k, now);
        while (kept.size() > CAP) {
            String first = kept.keySet().iterator().next();
            kept.remove(first);
        }
        persist();
    }

    private static boolean eligible(TLRPC.Message m) {
        if (m == null || m.out || m.ttl_period != 0) return false;
        if (m.action != null && !(m.action instanceof TLRPC.TL_messageActionEmpty)) return false;
        TLRPC.MessageMedia media = m.media;
        if (media == null || media instanceof TLRPC.TL_messageMediaEmpty || media instanceof TLRPC.TL_messageMediaWebPage) {
            return SealGram.pref("keep_text", true) && !TextUtils.isEmpty(m.message);
        }
        if (media.ttl_seconds != 0) return false;
        if (media instanceof TLRPC.TL_messageMediaPhoto) {
            return SealGram.pref("keep_photo", true) && media.photo != null;
        }
        if (media instanceof TLRPC.TL_messageMediaDocument) {
            return SealGram.pref("keep_voice", true) && media.document != null && MessageObject.isVoiceMessage(m);
        }
        return false;
    }

    public static void filterAsync(final MessagesController mc, final LongSparseArray<ArrayList<Integer>> deleted) {
        if (deleted == null || !enabled()) return;
        final int account = mc.currentAccount;
        final MessagesStorage st = MessagesStorage.getInstance(account);
        st.getStorageQueue().postRunnable(() -> {
            try {
                ArrayList<String> keys = new ArrayList<>();
                for (int a = 0, n = deleted.size(); a < n; a++) {
                    long key = deleted.keyAt(a);
                    ArrayList<Integer> ids = deleted.valueAt(a);
                    if (ids == null || ids.isEmpty()) continue;
                    if (key != 0 && DialogObject.isEncryptedDialog(key)) continue;
                    String join = TextUtils.join(",", new ArrayList<>(ids));
                    String sql = key == 0
                            ? "SELECT data FROM messages_v2 WHERE mid IN (" + join + ")"
                            : "SELECT data FROM messages_v2 WHERE uid = " + key + " AND mid IN (" + join + ")";
                    ArrayList<Integer> keep = new ArrayList<>();
                    SQLiteCursor cursor = null;
                    try {
                        cursor = st.getDatabase().queryFinalized(sql);
                        while (cursor.next()) {
                            NativeByteBuffer data = cursor.byteBufferValue(0);
                            if (data == null) continue;
                            try {
                                TLRPC.Message msg = TLRPC.Message.TLdeserialize(data, data.readInt32(false), false);
                                if (msg == null) continue;
                                msg.readAttachPath(data, UserConfig.getInstance(account).clientUserId);
                                if (key == 0 && msg.peer_id != null && msg.peer_id.channel_id != 0) continue;
                                if (!eligible(msg)) continue;
                                keep.add(msg.id);
                                keys.add(account + "_" + MessageObject.getDialogId(msg) + "_" + msg.id);
                            } finally {
                                data.reuse();
                            }
                        }
                    } finally {
                        if (cursor != null) cursor.dispose();
                    }
                    if (!keep.isEmpty()) {
                        ids.removeAll(keep);
                        if (ids.isEmpty()) ids.add(BOGUS_ID);
                    }
                }
                if (!keys.isEmpty()) remember(keys);
            } catch (Throwable t) {
                FileLog.e(t);
            }
            cleanupIfDue();
        });
    }

    public static final class Item {
        public int account;
        public long dialogId;
        public int mid;
        public long ts;
        public String text;
    }

    public static void loadItems(final int account, final int limit, final java.util.function.Consumer<ArrayList<Item>> cb) {
        load();
        final LinkedHashMap<Long, ArrayList<Integer>> byDialog = new LinkedHashMap<>();
        final HashMap<String, Long> stamps = new HashMap<>();
        synchronized (SealKeep.class) {
            ArrayList<java.util.Map.Entry<String, Long>> all = new ArrayList<>(kept.entrySet());
            int count = 0;
            for (int i = all.size() - 1; i >= 0 && count < limit; i--) {
                String[] p = all.get(i).getKey().split("_");
                if (p.length != 3) continue;
                try {
                    if (Integer.parseInt(p[0]) != account) continue;
                    long d = Long.parseLong(p[1]);
                    int mid = Integer.parseInt(p[2]);
                    ArrayList<Integer> list = byDialog.get(d);
                    if (list == null) {
                        list = new ArrayList<>();
                        byDialog.put(d, list);
                    }
                    list.add(mid);
                    stamps.put(d + "_" + mid, all.get(i).getValue());
                    count++;
                } catch (Throwable ignore) {
                }
            }
        }
        final MessagesStorage st = MessagesStorage.getInstance(account);
        st.getStorageQueue().postRunnable(() -> {
            final ArrayList<Item> items = new ArrayList<>();
            for (java.util.Map.Entry<Long, ArrayList<Integer>> e : byDialog.entrySet()) {
                SQLiteCursor cursor = null;
                try {
                    String join = TextUtils.join(",", e.getValue());
                    cursor = st.getDatabase().queryFinalized("SELECT data FROM messages_v2 WHERE uid = " + e.getKey() + " AND mid IN (" + join + ")");
                    while (cursor.next()) {
                        NativeByteBuffer data = cursor.byteBufferValue(0);
                        if (data == null) continue;
                        try {
                            TLRPC.Message msg = TLRPC.Message.TLdeserialize(data, data.readInt32(false), false);
                            if (msg == null) continue;
                            Item it = new Item();
                            it.account = account;
                            it.dialogId = e.getKey();
                            it.mid = msg.id;
                            Long ts = stamps.get(it.dialogId + "_" + it.mid);
                            it.ts = ts != null ? ts : msg.date * 1000L;
                            String t = msg.message;
                            if (TextUtils.isEmpty(t)) {
                                if (msg.media instanceof TLRPC.TL_messageMediaPhoto) t = "\uD83D\uDCF7 " + SealGramI18n.t("KeepPhotos");
                                else if (msg.media instanceof TLRPC.TL_messageMediaDocument) t = "\uD83C\uDFA4 " + SealGramI18n.t("KeepVoice");
                                else t = "";
                            }
                            it.text = t;
                            items.add(it);
                        } finally {
                            data.reuse();
                        }
                    }
                } catch (Throwable t) {
                    FileLog.e(t);
                } finally {
                    if (cursor != null) cursor.dispose();
                }
            }
            java.util.Collections.sort(items, (a, b) -> Long.compare(b.ts, a.ts));
            AndroidUtilities.runOnUIThread(() -> cb.accept(items));
        });
    }

    public static void cleanupIfDue() {
        long now = System.currentTimeMillis();
        if (now - lastCleanup < 60L * 60 * 1000) return;
        lastCleanup = now;
        cleanup(false);
    }

    public static void cleanup(boolean all) {
        try {
            load();
            ArrayList<String> expired = new ArrayList<>();
            synchronized (SealKeep.class) {
                long cutoff = System.currentTimeMillis() - days() * DAY;
                for (java.util.Map.Entry<String, Long> e : kept.entrySet()) {
                    if (all || e.getValue() < cutoff) expired.add(e.getKey());
                }
                for (String k : expired) kept.remove(k);
                if (!expired.isEmpty()) persist();
            }
            if (!expired.isEmpty()) purge(expired);
        } catch (Throwable t) {
            FileLog.e(t);
        }
    }

    private static void purge(ArrayList<String> keys) {
        HashMap<String, ArrayList<Integer>> groups = new HashMap<>();
        for (String k : keys) {
            String[] p = k.split("_");
            if (p.length != 3) continue;
            try {
                Integer.parseInt(p[0]);
                Long.parseLong(p[1]);
                int mid = Integer.parseInt(p[2]);
                String g = p[0] + "_" + p[1];
                ArrayList<Integer> list = groups.get(g);
                if (list == null) {
                    list = new ArrayList<>();
                    groups.put(g, list);
                }
                list.add(mid);
            } catch (Throwable ignore) {
            }
        }
        for (java.util.Map.Entry<String, ArrayList<Integer>> e : groups.entrySet()) {
            try {
                String[] p = e.getKey().split("_");
                final int acc = Integer.parseInt(p[0]);
                final long dialog = Long.parseLong(p[1]);
                final ArrayList<Integer> ids = e.getValue();
                if (!UserConfig.getInstance(acc).isClientActivated()) continue;
                TLRPC.Chat chat = dialog < 0 ? MessagesController.getInstance(acc).getChat(-dialog) : null;
                final long channelId = chat != null && ChatObject.isChannel(chat) ? -dialog : 0;
                final MessagesStorage st = MessagesStorage.getInstance(acc);
                st.getStorageQueue().postRunnable(() -> {
                    try {
                        ArrayList<Long> dialogIds = st.markMessagesAsDeleted(dialog, ids, false, true, 0, 0);
                        st.updateDialogsWithDeletedMessages(dialog, channelId, ids, dialogIds);
                    } catch (Throwable t) {
                        FileLog.e(t);
                    }
                    AndroidUtilities.runOnUIThread(() -> NotificationCenter.getInstance(acc).postNotificationName(NotificationCenter.messagesDeleted, ids, channelId, false));
                });
            } catch (Throwable t) {
                FileLog.e(t);
            }
        }
    }
}
