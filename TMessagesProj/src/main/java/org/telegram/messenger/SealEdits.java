package org.telegram.messenger;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLRPC;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public final class SealEdits {

    private static final int MAX_VERSIONS = 10;
    private static final int MAX_MESSAGES = 1500;
    private static final long DAY = 24L * 60 * 60 * 1000;

    private SealEdits() {
    }

    private static SharedPreferences store() {
        return ApplicationLoader.applicationContext.getSharedPreferences("sealgram_edits", 0);
    }

    private static String key(int account, long dialogId, int mid) {
        return account + "_" + dialogId + "_" + mid;
    }

    private static JSONArray read(String k) {
        try {
            String s = store().getString(k, null);
            if (s == null) return new JSONArray();
            JSONArray arr = new JSONArray(s);
            long cutoff = System.currentTimeMillis() - SealKeep.days() * DAY;
            JSONArray out = new JSONArray();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                if (o.getLong("t") >= cutoff) out.put(o);
            }
            return out;
        } catch (Throwable t) {
            return new JSONArray();
        }
    }

    public static boolean has(int account, long dialogId, int mid) {
        try {
            if (!SealGram.pref("edit_history", false)) return false;
            return read(key(account, dialogId, mid)).length() > 0;
        } catch (Throwable t) {
            return false;
        }
    }

    public static final class Entry {
        public long dialogId;
        public int mid;
        public long ts;
        public String text;
        public int versions;
    }

    public static ArrayList<Entry> entries(int account) {
        ArrayList<Entry> out = new ArrayList<>();
        try {
            String idx = store().getString("_keys", "");
            String[] keys = idx.split(",");
            for (int i = keys.length - 1; i >= 0 && out.size() < 200; i--) {
                String[] p = keys[i].split("_");
                if (p.length != 3 || Integer.parseInt(p[0]) != account) continue;
                JSONArray arr = read(keys[i]);
                if (arr.length() == 0) continue;
                JSONObject last = arr.getJSONObject(arr.length() - 1);
                Entry e = new Entry();
                e.dialogId = Long.parseLong(p[1]);
                e.mid = Integer.parseInt(p[2]);
                e.ts = last.getLong("t");
                e.text = last.getString("x");
                e.versions = arr.length();
                out.add(e);
            }
        } catch (Throwable ignore) {
        }
        return out;
    }

    public static String format(int account, long dialogId, int mid) {
        try {
            JSONArray arr = read(key(account, dialogId, mid));
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
            StringBuilder sb = new StringBuilder();
            for (int i = arr.length() - 1; i >= 0; i--) {
                JSONObject o = arr.getJSONObject(i);
                if (sb.length() > 0) sb.append("\n\n");
                sb.append(df.format(new Date(o.getLong("t")))).append('\n').append(o.getString("x"));
            }
            return sb.toString();
        } catch (Throwable t) {
            return "";
        }
    }

    public static void capture(final MessagesController mc, final TLRPC.Message m) {
        try {
            if (m == null || m.out || !SealGram.pref("edit_history", false)) return;
            final long dialog = m.dialog_id;
            final int mid = m.id;
            final String neu = m.message;
            if (dialog == 0 || DialogObject.isEncryptedDialog(dialog)) return;
            final int account = mc.currentAccount;
            final MessagesStorage st = MessagesStorage.getInstance(account);
            st.getStorageQueue().postRunnable(() -> {
                SQLiteCursor cursor = null;
                try {
                    cursor = st.getDatabase().queryFinalized("SELECT data FROM messages_v2 WHERE uid = " + dialog + " AND mid = " + mid);
                    if (!cursor.next()) return;
                    NativeByteBuffer data = cursor.byteBufferValue(0);
                    if (data == null) return;
                    String oldText;
                    long ts;
                    try {
                        TLRPC.Message old = TLRPC.Message.TLdeserialize(data, data.readInt32(false), false);
                        if (old == null) return;
                        oldText = old.message;
                        ts = (old.edit_date > 0 ? old.edit_date : old.date) * 1000L;
                    } finally {
                        data.reuse();
                    }
                    if (oldText == null || oldText.isEmpty() || oldText.equals(neu)) return;
                    String k = key(account, dialog, mid);
                    JSONArray arr = read(k);
                    JSONObject o = new JSONObject();
                    o.put("t", ts);
                    o.put("x", oldText);
                    arr.put(o);
                    JSONArray trimmed = new JSONArray();
                    for (int i = Math.max(0, arr.length() - MAX_VERSIONS); i < arr.length(); i++) trimmed.put(arr.get(i));
                    SharedPreferences sp = store();
                    String idx = sp.getString("_keys", "");
                    ArrayList<String> keys = new ArrayList<>();
                    for (String s : idx.split(",")) if (!s.isEmpty() && !s.equals(k)) keys.add(s);
                    keys.add(k);
                    SharedPreferences.Editor ed = sp.edit();
                    while (keys.size() > MAX_MESSAGES) ed.remove(keys.remove(0));
                    ed.putString(k, trimmed.toString());
                    ed.putString("_keys", android.text.TextUtils.join(",", keys));
                    ed.apply();
                } catch (Throwable t) {
                    FileLog.e(t);
                } finally {
                    if (cursor != null) cursor.dispose();
                }
            });
        } catch (Throwable t) {
            FileLog.e(t);
        }
    }

    public static void clear() {
        try {
            store().edit().clear().apply();
        } catch (Throwable ignore) {
        }
    }
}
