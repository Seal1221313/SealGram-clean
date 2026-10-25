package org.telegram.messenger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public final class SealFilter {

    private static final int CACHE_CAP = 6000;
    private static final int MAX_TEXT = 2000;
    private static volatile String cachedKey;
    private static volatile ArrayList<Pattern> cachedPatterns = new ArrayList<>();
    private static final HashMap<String, Boolean> verdicts = new HashMap<>();

    private SealFilter() {
    }

    public static boolean enabled() {
        return SealGram.pref("regex_enabled", false);
    }

    public static String raw() {
        return SealGram.strPref("regex_filters", "");
    }

    public static void setRaw(String value) {
        SealGram.setStrPref("regex_filters", value == null ? "" : value);
        cachedKey = null;
    }

    public static int count() {
        return patterns().size();
    }

    private static ArrayList<Pattern> patterns() {
        String raw = raw();
        boolean ci = SealGram.pref("regex_case", true);
        String key = (ci ? "1" : "0") + raw;
        if (!key.equals(cachedKey)) {
            ArrayList<Pattern> list = new ArrayList<>();
            int flags = Pattern.MULTILINE | (ci ? Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE : 0);
            for (String line : raw.split("\n")) {
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    list.add(Pattern.compile(line, flags));
                } catch (Throwable ignore) {
                }
            }
            synchronized (verdicts) {
                verdicts.clear();
                cachedPatterns = list;
                cachedKey = key;
            }
        }
        return cachedPatterns;
    }

    public static boolean hides(MessageObject msg, org.telegram.tgnet.TLRPC.Chat chat) {
        try {
            if (msg == null || msg.messageOwner == null || !enabled()) return false;
            if (!(ChatObject.isChannel(chat) || SealGram.pref("regex_chats", false))) return false;
            if (msg.isOutOwner()) return false;
            ArrayList<Pattern> list = patterns();
            if (list.isEmpty()) return false;
            String key = msg.currentAccount + "_" + msg.getDialogId() + "_" + msg.getId() + "_" + msg.messageOwner.edit_date;
            synchronized (verdicts) {
                Boolean cached = verdicts.get(key);
                if (cached != null) return cached;
            }
            String text = msg.messageOwner.message;
            boolean hit = false;
            if (text != null && !text.isEmpty()) {
                if (text.length() > MAX_TEXT) text = text.substring(0, MAX_TEXT);
                for (int i = 0; i < list.size() && !hit; i++) {
                    hit = list.get(i).matcher(text).find();
                }
            }
            synchronized (verdicts) {
                if (verdicts.size() > CACHE_CAP) verdicts.clear();
                verdicts.put(key, hit);
            }
            return hit;
        } catch (Throwable t) {
            return false;
        }
    }
}
