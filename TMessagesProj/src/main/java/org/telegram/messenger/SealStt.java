package org.telegram.messenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.json.JSONObject;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.SealGramHubActivity;
import org.telegram.ui.Components.TranscribeButton;
import org.telegram.tgnet.TLRPC;
import org.vosk.Model;
import org.vosk.Recognizer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class SealStt {

    public interface Progress {
        void update(String text, int percent);
    }

    private static final String[][] LANGS = {
            {"uk", "Українська (легка)", "vosk-model-small-uk-v3-nano", "73"},
            {"ukb", "Українська (точніша)", "vosk-model-small-uk-v3-small", "133"},
            {"ru", "Російська", "vosk-model-small-ru-0.22", "45"},
            {"en", "Англійська", "vosk-model-small-en-us-0.15", "40"},
            {"pl", "Польська", "vosk-model-small-pl-0.22", "50"},
            {"de", "Німецька", "vosk-model-small-de-0.15", "45"}
    };

    private static volatile boolean busy;
    private static volatile boolean recognizing;

    private static SharedPreferences prefs() {
        return ApplicationLoader.applicationContext.getSharedPreferences("sealgram", Context.MODE_PRIVATE);
    }

    public static String[] codes() {
        String[] r = new String[LANGS.length];
        for (int i = 0; i < LANGS.length; i++) r[i] = LANGS[i][0];
        return r;
    }

    private static String[] find(String code) {
        for (String[] l : LANGS) if (l[0].equals(code)) return l;
        return LANGS[0];
    }

    public static String langName(String code) {
        switch (code) {
            case "uk": return SealGramI18n.t("SttLangUkrainianEasy");
            case "ukb": return SealGramI18n.t("SttLangUkrainianAccurate");
            case "ru": return SealGramI18n.t("SttLangRussian");
            case "en": return SealGramI18n.t("SttLangEnglish");
            case "pl": return SealGramI18n.t("SttLangPolish");
            case "de": return SealGramI18n.t("SttLangGerman");
            default: return find(code)[1];
        }
    }

    public static String sizeMb(String code) {
        return find(code)[3];
    }

    public static String lang() {
        return prefs().getString("stt_lang", "uk");
    }

    public static void setLang(String code) {
        prefs().edit().putString("stt_lang", code).apply();
    }

    public static File modelDir(String code) {
        return new File(new File(ApplicationLoader.applicationContext.getFilesDir(), "sealstt"), find(code)[2]);
    }

    private static boolean hasModelFiles(File dir) {
        if (dir == null || !dir.isDirectory()) return false;
        File acoustic = new File(dir, "am/final.mdl");
        File config = new File(dir, "conf/model.conf");
        return acoustic.isFile() && acoustic.length() > 0 && config.isFile() && config.length() > 0;
    }

    /*
     * Also recognises model folders created by older SealGram builds or by unzip tools
     * that preserve an extra top-level directory. The model's real root is returned so
     * the recogniser opens exactly the directory whose files were checked.
     */
    private static File findModelRoot(File dir, int depth) {
        if (hasModelFiles(dir)) return dir;
        if (dir == null || depth >= 3) return null;
        File[] children = dir.listFiles();
        if (children == null) return null;
        for (File child : children) {
            if (child.isDirectory()) {
                File found = findModelRoot(child, depth + 1);
                if (found != null) return found;
            }
        }
        return null;
    }

    public static File installedModelDir(String code) {
        return findModelRoot(modelDir(code), 0);
    }

    public static boolean installed(String code) {
        return installedModelDir(code) != null;
    }

    public static boolean isBusy() {
        return busy;
    }

    private static boolean online() {
        try {
            ConnectivityManager cm = (ConnectivityManager) ApplicationLoader.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null && ni.isConnected();
        } catch (Throwable t) {
            return true;
        }
    }

    private static void deleteRec(File f) {
        if (f == null || !f.exists()) return;
        File[] c = f.listFiles();
        if (c != null) for (File x : c) deleteRec(x);
        f.delete();
    }

    public static void delete(String code) {
        deleteRec(modelDir(code));
    }

    public static void download(String code, Progress progress, Runnable done) {
        if (busy) return;
        final String[] l = find(code);
        if (!online()) {
            post(progress, SealGramI18n.t("SttNoInternet"), -1);
            return;
        }
        busy = true;
        new Thread(() -> {
            File zip = null;
            File tmp = null;
            String error = null;
            try {
                File base = new File(ApplicationLoader.applicationContext.getFilesDir(), "sealstt");
                base.mkdirs();
                zip = new File(ApplicationLoader.applicationContext.getCacheDir(), l[2] + ".zip");
                tmp = new File(base, ".tmp_" + l[2]);
                deleteRec(tmp);
                HttpURLConnection c = (HttpURLConnection) new URL("https://alphacephei.com/vosk/models/" + l[2] + ".zip").openConnection();
                c.setInstanceFollowRedirects(true);
                c.setConnectTimeout(20000);
                c.setReadTimeout(30000);
                int http = c.getResponseCode();
                if (http != 200) throw new IllegalStateException(SealGramI18n.format("SttHttpError", http));
                long total = c.getContentLengthLong();
                long expectedMin = Long.parseLong(l[3]) * 1024L * 1024L * 7 / 10;
                if (total > 0 && total < expectedMin) throw new IllegalStateException(SealGramI18n.t("SttWrongRemoteSize"));
                long got = 0;
                try (InputStream in = c.getInputStream(); OutputStream out = new FileOutputStream(zip)) {
                    byte[] buf = new byte[65536];
                    int n;
                    int last = -1;
                    while ((n = in.read(buf)) > 0) {
                        out.write(buf, 0, n);
                        got += n;
                        int pct = total > 0 ? (int) (got * 100 / total) : -1;
                        if (pct != last) {
                            last = pct;
                            post(progress, SealGramI18n.format("SttDownloading", (got / 1048576) + (total > 0 ? " / " + (total / 1048576) : "") + " " + SealGramI18n.t("UnitMb")), pct);
                        }
                    }
                }
                if (total > 0 && got != total) throw new IllegalStateException(SealGramI18n.t("SttDownloadInterrupted"));
                if (got < expectedMin) throw new IllegalStateException(SealGramI18n.t("SttFileTooSmall"));
                post(progress, SealGramI18n.t("SttCheckingArchive"), -1);
                try (ZipFile zf = new ZipFile(zip)) {
                    if (zf.getEntry(l[2] + "/am/final.mdl") == null) throw new IllegalStateException(SealGramI18n.t("SttArchiveMissingModel"));
                }
                post(progress, SealGramI18n.t("SttUnpacking"), -1);
                tmp.mkdirs();
                String tmpPath = tmp.getCanonicalPath() + File.separator;
                try (ZipInputStream zin = new ZipInputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(zip)))) {
                    ZipEntry e;
                    byte[] buf = new byte[65536];
                    while ((e = zin.getNextEntry()) != null) {
                        File f = new File(tmp, e.getName());
                        if (!f.getCanonicalPath().startsWith(tmpPath)) continue;
                        if (e.isDirectory()) {
                            f.mkdirs();
                            continue;
                        }
                        f.getParentFile().mkdirs();
                        try (OutputStream out = new FileOutputStream(f)) {
                            int n;
                            while ((n = zin.read(buf)) > 0) out.write(buf, 0, n);
                        }
                    }
                }
                File extracted = new File(tmp, l[2]);
                if (!new File(extracted, "am/final.mdl").exists() || !new File(extracted, "conf/model.conf").exists()) {
                    throw new IllegalStateException(SealGramI18n.t("SttModelIncomplete"));
                }
                post(progress, SealGramI18n.t("SttCheckingModel"), -1);
                try (Model m = new Model(extracted.getAbsolutePath())) {
                    // модель успішно завантажується рушієм
                }
                File target = modelDir(code);
                deleteRec(target);
                if (!extracted.renameTo(target)) throw new IllegalStateException(SealGramI18n.t("SttSaveFailed"));
            } catch (Throwable t) {
                error = t.getMessage() != null ? t.getMessage() : t.toString();
            } finally {
                if (zip != null) zip.delete();
                deleteRec(tmp);
                busy = false;
            }
            if (error != null) {
                deleteRec(modelDir(code));
                post(progress, SealGramI18n.t("SttErrorPrefix") + error, -1);
            } else {
                post(progress, SealGramI18n.t("SttModelInstalled"), 100);
            }
            AndroidUtilities.runOnUIThread(() -> {
                if (done != null) done.run();
            });
        }, "SealSttDownload").start();
    }

    private static void post(Progress p, String text, int percent) {
        if (p != null) AndroidUtilities.runOnUIThread(() -> p.update(text, percent));
    }

    public static void recognize(BaseFragment fragment, MessageObject msg) {
        if (fragment == null || fragment.getParentActivity() == null || msg == null) return;
        final Context ctx = fragment.getParentActivity();
        final String code = lang();
        if (!installed(code)) {
            new AlertDialog.Builder(ctx)
                    .setTitle(SealGramI18n.t("SttDialogTitle"))
                    .setMessage(SealGramI18n.format("SttModelNotInstalled", langName(code), sizeMb(code)))
                    .setPositiveButton(SealGramI18n.t("SttOpen"), (d, w) -> fragment.presentFragment(new SealGramHubActivity.Page(4)))
                    .setNegativeButton(SealGramI18n.t("CommonCancel"), null)
                    .show();
            return;
        }
        File f = findAudioFile(msg);
        if (f == null) {
            Toast.makeText(ctx, SealGramI18n.t("SttNotFoundLocalVoice"), Toast.LENGTH_LONG).show();
            return;
        }
        if (recognizing) {
            Toast.makeText(ctx, SealGramI18n.t("SttRecognitionBusy"), Toast.LENGTH_SHORT).show();
            return;
        }
        recognizing = true;
        Toast.makeText(ctx, SealGramI18n.t("SttRecognizing"), Toast.LENGTH_SHORT).show();
        final String path = f.getAbsolutePath();
        new Thread(() -> {
            String text = null;
            String error = null;
            try {
                byte[] pcm = decode(path);
                File activeModel = installedModelDir(code);
                if (activeModel == null) throw new IllegalStateException(SealGramI18n.t("SttModelNotFoundStorage"));
                text = run(pcm, activeModel.getAbsolutePath());
            } catch (Throwable t) {
                error = t.getMessage() != null ? t.getMessage() : t.toString();
            } finally {
                recognizing = false;
            }
            final String shown = error != null ? SealGramI18n.format("SttRecognitionFailed", error) : (text == null || text.trim().isEmpty() ? SealGramI18n.t("SttLanguageUndetected") : text.trim());
            final boolean ok = error == null && text != null && !text.trim().isEmpty();
            AndroidUtilities.runOnUIThread(() -> {
                AlertDialog.Builder b = new AlertDialog.Builder(ctx)
                        .setTitle(SealGramI18n.t("SttRecognizedText"))
                        .setMessage(shown)
                        .setPositiveButton(SealGramI18n.t("CommonOK"), null);
                if (ok) {
                    b.setNegativeButton(SealGramI18n.t("SttCopy"), (d, w) -> AndroidUtilities.addToClipboard(shown));
                }
                b.show();
            });
        }, "SealSttRecognize").start();
    }


    /**
     * Called by the transcript button inside a voice-message bubble. It never starts audio
     * playback: it recognises the local file and puts the result into Telegram's transcript
     * area so the user can read it directly in the chat.
     */
    public static boolean recognizeInline(Context ctx, MessageObject msg, Runnable finished) {
        if (ctx == null || msg == null || msg.messageOwner == null) return false;

        final String code = lang();
        if (!installed(code)) {
            AndroidUtilities.runOnUIThread(() -> {
                new AlertDialog.Builder(ctx)
                        .setTitle(SealGramI18n.t("SttOfflineTitle"))
                        .setMessage(SealGramI18n.format("SttInlineModelNotFound", langName(code)))
                        .setPositiveButton(SealGramI18n.t("SttUnderstand"), null)
                        .show();
                if (finished != null) finished.run();
            });
            return true;
        }
        if (recognizing) {
            AndroidUtilities.runOnUIThread(() -> {
                Toast.makeText(ctx, SealGramI18n.t("SttRecognitionBusy"), Toast.LENGTH_SHORT).show();
                if (finished != null) finished.run();
            });
            return true;
        }

        File audio = findAudioFile(msg);
        if (audio == null) {
            AndroidUtilities.runOnUIThread(() -> {
                Toast.makeText(ctx, SealGramI18n.t("SttAudioNotFound"), Toast.LENGTH_LONG).show();
                if (finished != null) finished.run();
            });
            return true;
        }

        final File source = audio;
        final File model = installedModelDir(code);
        recognizing = true;
        Toast.makeText(ctx, SealGramI18n.t("SttRecognizing"), Toast.LENGTH_SHORT).show();
        new Thread(() -> {
            String text = null;
            String error = null;
            try {
                text = run(decode(source.getAbsolutePath()), model.getAbsolutePath());
            } catch (Throwable t) {
                error = t.getMessage() != null ? t.getMessage() : t.toString();
            } finally {
                recognizing = false;
            }

            final String result = text == null ? "" : text.trim();
            final String failure = error;
            AndroidUtilities.runOnUIThread(() -> {
                if (failure == null && !result.isEmpty()) {
                    msg.messageOwner.voiceTranscription = result;
                    msg.messageOwner.voiceTranscriptionOpen = true;
                    msg.messageOwner.voiceTranscriptionFinal = true;
                    TranscribeButton.openVideoTranscription(msg);
                    TranscribeButton.finishTranscription(msg, 0, result);
                } else {
                    Toast.makeText(ctx,
                            failure != null ? SealGramI18n.format("SttRecognitionFailed", failure) : SealGramI18n.t("SttLanguageUndetected"),
                            Toast.LENGTH_LONG).show();
                }
                if (finished != null) finished.run();
            });
        }, "SealSttInline").start();
        return true;
    }

    private static void addCandidate(List<File> candidates, File file) {
        if (file != null) candidates.add(file);
    }

    private static void addPathCandidate(List<File> candidates, String path) {
        if (path != null && !path.trim().isEmpty()) candidates.add(new File(path));
    }

    /*
     * Telegram can store already-downloaded voice notes under the audio folder, in the
     * cache folder, or at a message's local attachPath. Try each canonical FileLoader path
     * before claiming that the user has not downloaded the audio.
     */
    private static File findAudioFile(MessageObject msg) {
        if (msg == null || msg.messageOwner == null) return null;
        ArrayList<File> candidates = new ArrayList<>();
        try {
            addPathCandidate(candidates, msg.messageOwner.attachPath);
        } catch (Throwable ignore) {
        }
        try {
            TLRPC.Document document = msg.getDocument();
            if (document != null) {
                addPathCandidate(candidates, document.localPath);
            }
            FileLoader loader = FileLoader.getInstance(msg.currentAccount);
            addCandidate(candidates, loader.getPathToMessage(msg.messageOwner));
            addCandidate(candidates, loader.getPathToMessage(msg.messageOwner, false, false));
            addCandidate(candidates, loader.getPathToMessage(msg.messageOwner, true, true));
            if (document != null) {
                addCandidate(candidates, loader.getPathToAttach(document));
                addCandidate(candidates, loader.getPathToAttach(document, null, true));
                addCandidate(candidates, loader.getPathToAttach(document, null, false, true));
                addCandidate(candidates, loader.getPathToAttach(document, null, true, true));
            }
        } catch (Throwable ignore) {
        }
        for (File candidate : candidates) {
            try {
                if (candidate.isFile() && candidate.canRead() && candidate.length() > 0) return candidate;
            } catch (Throwable ignore) {
            }
        }
        return null;
    }

    private static String run(byte[] pcm, String modelPath) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (Model model = new Model(modelPath); Recognizer rec = new Recognizer(model, 16000.0f)) {
            int off = 0;
            while (off < pcm.length) {
                int n = Math.min(8000, pcm.length - off);
                byte[] chunk = Arrays.copyOfRange(pcm, off, off + n);
                if (rec.acceptWaveForm(chunk, n)) {
                    append(sb, rec.getResult());
                }
                off += n;
            }
            append(sb, rec.getFinalResult());
        }
        return sb.toString();
    }

    private static void append(StringBuilder sb, String json) {
        try {
            String t = new JSONObject(json).optString("text", "");
            if (!t.isEmpty()) {
                if (sb.length() > 0) sb.append(' ');
                sb.append(t);
            }
        } catch (Throwable ignore) {
        }
    }

    private static byte[] decode(String path) throws Exception {
        MediaExtractor ex = new MediaExtractor();
        MediaCodec codec = null;
        try {
            ex.setDataSource(path);
            int track = -1;
            MediaFormat fmt = null;
            for (int i = 0; i < ex.getTrackCount(); i++) {
                MediaFormat f = ex.getTrackFormat(i);
                String m = f.getString(MediaFormat.KEY_MIME);
                if (m != null && m.startsWith("audio/")) {
                    track = i;
                    fmt = f;
                    break;
                }
            }
            if (track < 0) throw new IllegalStateException(SealGramI18n.t("SttAudioTrackMissing"));
            ex.selectTrack(track);
            int sr = fmt.containsKey(MediaFormat.KEY_SAMPLE_RATE) ? fmt.getInteger(MediaFormat.KEY_SAMPLE_RATE) : 48000;
            int ch = fmt.containsKey(MediaFormat.KEY_CHANNEL_COUNT) ? fmt.getInteger(MediaFormat.KEY_CHANNEL_COUNT) : 1;
            codec = MediaCodec.createDecoderByType(fmt.getString(MediaFormat.KEY_MIME));
            codec.configure(fmt, null, null, 0);
            codec.start();
            ByteArrayOutputStream pcm = new ByteArrayOutputStream();
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
            boolean inDone = false, outDone = false;
            while (!outDone) {
                if (!inDone) {
                    int ii = codec.dequeueInputBuffer(10000);
                    if (ii >= 0) {
                        ByteBuffer b = codec.getInputBuffer(ii);
                        int n = ex.readSampleData(b, 0);
                        if (n < 0) {
                            codec.queueInputBuffer(ii, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                            inDone = true;
                        } else {
                            codec.queueInputBuffer(ii, 0, n, ex.getSampleTime(), 0);
                            ex.advance();
                        }
                    }
                }
                int oi = codec.dequeueOutputBuffer(info, 10000);
                if (oi >= 0) {
                    ByteBuffer ob = codec.getOutputBuffer(oi);
                    if (ob != null && info.size > 0) {
                        byte[] chunk = new byte[info.size];
                        ob.position(info.offset);
                        ob.get(chunk);
                        pcm.write(chunk);
                    }
                    codec.releaseOutputBuffer(oi, false);
                    if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) outDone = true;
                } else if (oi == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    MediaFormat of = codec.getOutputFormat();
                    if (of.containsKey(MediaFormat.KEY_SAMPLE_RATE)) sr = of.getInteger(MediaFormat.KEY_SAMPLE_RATE);
                    if (of.containsKey(MediaFormat.KEY_CHANNEL_COUNT)) ch = of.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
                }
            }
            byte[] raw = pcm.toByteArray();
            java.nio.ShortBuffer sb = ByteBuffer.wrap(raw).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
            short[] s = new short[sb.remaining()];
            sb.get(s);
            int frames = s.length / Math.max(1, ch);
            if (frames == 0) throw new IllegalStateException(SealGramI18n.t("SttAudioEmpty"));
            double ratio = sr / 16000.0;
            int outLen = (int) (frames / ratio);
            byte[] out = new byte[outLen * 2];
            for (int i = 0; i < outLen; i++) {
                int i0 = Math.min((int) (i * ratio), frames - 1);
                int sum = 0;
                for (int c = 0; c < ch; c++) sum += s[i0 * ch + c];
                short v = (short) (sum / ch);
                out[i * 2] = (byte) (v & 0xff);
                out[i * 2 + 1] = (byte) ((v >> 8) & 0xff);
            }
            return out;
        } finally {
            try {
                if (codec != null) {
                    codec.stop();
                    codec.release();
                }
            } catch (Throwable ignore) {
            }
            ex.release();
        }
    }
}
