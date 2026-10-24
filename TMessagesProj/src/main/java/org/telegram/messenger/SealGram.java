package org.telegram.messenger;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.ApplicationExitInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;

public class SealGram {

    public static final String SITE = "https://seal-hub.pages.dev";
    public static final String DISCLAIMER = "SealGram — неофіційний клієнт, не пов'язаний з Telegram.";

    private static volatile SharedPreferences prefsInstance;

    private static SharedPreferences prefs() {
        SharedPreferences p = prefsInstance;
        if (p == null) {
            p = ApplicationLoader.applicationContext.getSharedPreferences("sealgram", Context.MODE_PRIVATE);
            prefsInstance = p;
        }
        return p;
    }

    public static boolean ghost() {
        return prefs().getBoolean("ghost", false);
    }

    public static void setGhost(boolean value) {
        boolean changed = ghost() != value;
        prefs().edit().putBoolean("ghost", value).apply();
        if (changed) {
            try {
                MessagesController.getInstance(UserConfig.selectedAccount).onGhostModeChanged(value);
            } catch (Throwable ignore) {
                // The preference is still saved if the controller is not ready yet.
            }
        }
    }

    private static boolean sub(String key) {
        return ghost() && prefs().getBoolean(key, true);
    }

    public static boolean ghostOnline() {
        return sub("ghost_online");
    }

    public static boolean ghostTyping() {
        return sub("ghost_typing");
    }

    public static boolean ghostRead() {
        return sub("ghost_read");
    }

    public static boolean getSub(String key) {
        return prefs().getBoolean(key, true);
    }

    public static void setSub(String key, boolean value) {
        prefs().edit().putBoolean(key, value).apply();
        try {
            MessagesController.getInstance(UserConfig.selectedAccount).onGhostModeChanged(ghostOnline());
        } catch (Throwable ignore) {
        }
    }

    public static String registrationText(TLRPC.User user) {
        if (user == null) return "—";
        return SealGramI18n.format("ObjectRegistration", estimateUserCreationYear(user.id));
    }

    public static String idLine(int account, TLRPC.User user) {
        if (user == null) return "";
        int dc = 0;
        if (user.photo != null) dc = user.photo.dc_id;
        if (dc <= 0 && user.self) {
            try {
                dc = ConnectionsManager.getInstance(account).getCurrentDatacenterId();
            } catch (Throwable ignore) {
            }
        }
        return SealGramI18n.format("ObjectIdLine", user.id, dc > 0 ? "  ·  DC" + dc : "", SealGramI18n.t("ObjectTapToCopy"));
    }

    private static final java.util.regex.Pattern BRAND = java.util.regex.Pattern.compile("Telegram(?! (Premium|Stars?|Business|Passport|Wallet|Ads|Ad Platform))");

    public static String brand(String value) {
        if (value == null || value.indexOf("Telegram") < 0) return value;
        if (value.contains("://") || value.contains("t.me") || value.contains("telegram.org") || value.contains("telegram.me") || value.contains("@telegram") || value.contains("tg:")) return value;
        return BRAND.matcher(value).replaceAll("SealGram");
    }

    public static boolean pref(String key, boolean def) {
        return prefs().getBoolean(key, def);
    }

    public static int intPref(String key, int def) {
        return prefs().getInt(key, def);
    }

    public static void setIntPref(String key, int value) {
        prefs().edit().putInt(key, value).apply();
    }

    private static final boolean[] archiveOpened = new boolean[UserConfig.MAX_ACCOUNT_COUNT];

    public static boolean archiveLazy(int account) {
        return account >= 0 && account < archiveOpened.length && !archiveOpened[account] && pref("lazy_archive", false);
    }

    public static void unlockArchive(int account) {
        if (account >= 0 && account < archiveOpened.length) archiveOpened[account] = true;
    }

    public static boolean ghostDelay() {
        return ghost() && prefs().getBoolean("ghost_delay", false);
    }

    public static void applyGhostDelay(SendMessagesHelper.SendMessageParams p) {
        try {
            if (p == null || p.scheduleDate != 0 || !ghostDelay() || p.retryMessageObject != null) return;
            if (p.peer == 0 || DialogObject.isEncryptedDialog(p.peer)) return;
            if (p.peer == UserConfig.getInstance(UserConfig.selectedAccount).getClientUserId()) return;
            p.scheduleDate = ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime() + Math.max(12, intPref("ghost_delay_sec", 12));
        } catch (Throwable ignore) {
        }
    }

    public static boolean flag(String key) {
        return prefs().getBoolean(key, false);
    }

    public static void setPref(String key, boolean value) {
        prefs().edit().putBoolean(key, value).apply();
    }

    public static boolean hideOwnPhoneInProfile() {
        return prefs().getBoolean("hide_own_phone_in_profile", false);
    }

    public static void setHideOwnPhoneInProfile(boolean value) {
        prefs().edit().putBoolean("hide_own_phone_in_profile", value).apply();
    }

    private static String estimateUserCreationYear(long userId) {
        // Telegram does not expose account creation timestamps. This is a low-confidence
        // community-style ID-range heuristic, not a verified date.
        if (userId <= 0) return SealGramI18n.t("ObjectUnknown");
        if (userId < 1_000_000L) return "2013–2014";
        if (userId < 10_000_000L) return "2014–2015";
        if (userId < 100_000_000L) return "2015–2016";
        if (userId < 500_000_000L) return "2016–2017";
        if (userId < 1_000_000_000L) return "2017–2018";
        if (userId < 2_000_000_000L) return "2018–2019";
        if (userId < 3_000_000_000L) return "2019–2020";
        if (userId < 4_000_000_000L) return "2020";
        if (userId < 5_000_000_000L) return "2021";
        if (userId < 6_000_000_000L) return "2022";
        if (userId < 7_000_000_000L) return "2023";
        if (userId < 8_000_000_000L) return "2024";
        if (userId < 9_000_000_000L) return "2025";
        return "2025–2026";
    }

    public static void showObjectInspector(Context context, int account, long userId, long chatId,
                                           TLRPC.User user, TLRPC.Chat chat,
                                           TLRPC.UserFull userInfo, TLRPC.ChatFull chatInfo) {
        if (context == null) return;
        final String peerId = chat != null ? Long.toString(-chat.id) :
                (chatId != 0 ? Long.toString(-chatId) : Long.toString(user != null ? user.id : userId));
        final String entityId = user != null ? Long.toString(user.id) :
                (chat != null ? Long.toString(chat.id) : (chatId != 0 ? Long.toString(chatId) : Long.toString(userId)));
        String json;
        try {
            org.json.JSONObject root = new org.json.JSONObject();
            org.json.JSONObject flags = new org.json.JSONObject();
            root.put("client", "SealGram");
            root.put("account_slot", account);
            root.put("peer_type", user != null ? "user" : (chat != null ? (chat.broadcast ? "channel" : "group") : "unknown"));
            root.put("entity_id", entityId);
            root.put("dialog_id", peerId);
            root.put("telegram_creation_date", "Exact user account creation date is not exposed by Telegram API");
            if (user != null) {
                root.put("display_name", UserObject.getUserName(user));
                String username = UserObject.getPublicUsername(user);
                if (username != null) root.put("username", username);
                root.put("profile_photo_present", UserObject.hasPhoto(user));
                root.put("phone_number_present", user.phone != null && !user.phone.isEmpty());
                root.put("about_present", userInfo != null && userInfo.about != null && !userInfo.about.isEmpty());
                root.put("approximate_creation_year", estimateUserCreationYear(user.id));
                root.put("creation_year_confidence", "low; inferred from ID range, not verified by Telegram");
                flags.put("verified", user.verified);
                flags.put("restricted", user.restricted);
                flags.put("scam", user.scam);
                flags.put("fake", user.fake);
                flags.put("premium", user.premium);
                flags.put("bot", user.bot);
                flags.put("deleted", UserObject.isDeleted(user));
                flags.put("self", UserObject.isUserSelf(user));
                flags.put("inactive", user.inactive);
                flags.put("explicit_content", user.explicit_content);
                flags.put("restriction_reason_count", user.restriction_reason == null ? 0 : user.restriction_reason.size());
            } else if (chat != null) {
                root.put("display_name", chat.title);
                if (chat.username != null && !chat.username.isEmpty()) root.put("username", chat.username);
                if (chat.date > 0) {
                    root.put("server_chat_date_unix", chat.date);
                } else {
                    root.put("server_chat_date_unix", org.json.JSONObject.NULL);
                }
                root.put("about_present", chatInfo != null && chatInfo.about != null && !chatInfo.about.isEmpty());
                root.put("participants_count", chat.participants_count);
                flags.put("verified", chat.verified);
                flags.put("restricted", chat.restricted);
                flags.put("scam", chat.scam);
                flags.put("fake", chat.fake);
                flags.put("broadcast", chat.broadcast);
                flags.put("megagroup", chat.megagroup);
                flags.put("gigagroup", chat.gigagroup);
                flags.put("creator", chat.creator);
                flags.put("left", chat.left);
                flags.put("kicked", chat.kicked);
                flags.put("noforwards", chat.noforwards);
                flags.put("forum", chat.forum);
                flags.put("restriction_reason_count", chat.restriction_reason == null ? 0 : chat.restriction_reason.size());
            }
            root.put("flags", flags);
            root.put("excluded", new org.json.JSONArray().put("data-center IDs").put("media hashes"));
            json = root.toString(2);
        } catch (Exception e) {
            json = "{\"error\":\"Unable to build object report\"}";
        }
        final String report = json;
        new AlertDialog.Builder(context)
                .setTitle(SealGramI18n.t("ObjectInspectorTitle"))
                .setMessage(report)
                .setPositiveButton(SealGramI18n.t("ObjectCopyJson"), (dialog, which) -> {
                    try {
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        if (clipboard != null) clipboard.setPrimaryClip(ClipData.newPlainText("SealGram object info", report));
                    } catch (Throwable ignore) {
                    }
                })
                .setNeutralButton(SealGramI18n.t("ObjectCopyId"), (dialog, which) -> {
                    try {
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                        if (clipboard != null) clipboard.setPrimaryClip(ClipData.newPlainText("Telegram peer ID", peerId));
                    } catch (Throwable ignore) {
                    }
                })
                .setNegativeButton(SealGramI18n.t("ObjectClose"), null)
                .show();
    }

    public static void showLastCrash(Activity activity) {
    }

    private static void openReport(Context context, String report) {
        try {
            if (report.length() > 20000) {
                report = report.substring(0, 20000);
            }
            Intent intent = new Intent(context, SealGramCrashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("report", report);
            context.startActivity(intent);
        } catch (Throwable ignore) {
        }
    }

    private static boolean isMainProcess(Application app) {
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                String name = Application.getProcessName();
                return name == null || name.equals(app.getPackageName());
            }
        } catch (Throwable ignore) {
        }
        return true;
    }

    public static void installCrashHandler(final Application app) {
        try {
            if (!isMainProcess(app)) {
                return;
            }
            final Thread.UncaughtExceptionHandler previous = Thread.getDefaultUncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
                try {
                    java.io.StringWriter sw = new java.io.StringWriter();
                    throwable.printStackTrace(new java.io.PrintWriter(sw));
                    openReport(app, "JAVA CRASH in thread " + thread.getName() + "\n" + Build.MODEL + " Android " + Build.VERSION.RELEASE + "\n\n" + sw);
                    Thread.sleep(700);
                } catch (Throwable ignore) {
                }
                if (previous != null) {
                    previous.uncaughtException(thread, throwable);
                } else {
                    System.exit(10);
                }
            });
        } catch (Throwable ignore) {
        }
    }

    public static void reportPreviousExit(final Application app) {
        try {
            if (Build.VERSION.SDK_INT < 30 || !isMainProcess(app)) {
                return;
            }
            ActivityManager am = (ActivityManager) app.getSystemService(Context.ACTIVITY_SERVICE);
            java.util.List<ApplicationExitInfo> list = am.getHistoricalProcessExitReasons(app.getPackageName(), 0, 3);
            if (list == null || list.isEmpty()) {
                return;
            }
            SharedPreferences p = prefs();
            long seen = p.getLong("exit_seen", 0);
            p.edit().putLong("exit_seen", list.get(0).getTimestamp()).apply();
            StringBuilder sb = new StringBuilder();
            for (ApplicationExitInfo info : list) {
                if (info.getTimestamp() <= seen) {
                    continue;
                }
                int r = info.getReason();
                if (r != ApplicationExitInfo.REASON_CRASH_NATIVE && r != ApplicationExitInfo.REASON_ANR
                        && r != ApplicationExitInfo.REASON_INITIALIZATION_FAILURE && r != ApplicationExitInfo.REASON_SIGNALED
                        && r != ApplicationExitInfo.REASON_DEPENDENCY_DIED) {
                    continue;
                }
                sb.append("PREVIOUS EXIT reason=").append(r).append(" status=").append(info.getStatus()).append("\n");
                sb.append("description: ").append(info.getDescription()).append("\n");
                try {
                    java.io.InputStream in = info.getTraceInputStream();
                    if (in != null) {
                        byte[] buf = new byte[300000];
                        int n = 0;
                        int c;
                        while (n < buf.length && (c = in.read(buf, n, buf.length - n)) > 0) {
                            n += c;
                        }
                        in.close();
                        java.util.regex.Matcher m = java.util.regex.Pattern.compile("[\\x20-\\x7e]{8,}").matcher(new String(buf, 0, n, java.nio.charset.StandardCharsets.ISO_8859_1));
                        int lines = 0;
                        while (m.find() && lines < 150 && sb.length() < 15000) {
                            sb.append(m.group()).append("\n");
                            lines++;
                        }
                    }
                } catch (Throwable ignore) {
                }
                sb.append("\n");
            }
            if (sb.length() == 0) {
                return;
            }
            final String report = sb.toString();
            new Handler(Looper.getMainLooper()).postDelayed(() -> openReport(app, report), 2500);
        } catch (Throwable ignore) {
        }
    }
}
