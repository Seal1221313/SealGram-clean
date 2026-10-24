package org.telegram.messenger;

import android.graphics.Typeface;

import java.io.File;

public final class SealFont {

    private static Typeface base;
    private static boolean loaded;
    private static int enabled = -1;
    private static final Typeface[] styles = new Typeface[4];

    private SealFont() {
    }

    public static File file() {
        return new File(ApplicationLoader.getFilesDirFixed(), "sealgram_font.ttf");
    }

    public static boolean valid(File f) {
        try {
            return Typeface.createFromFile(f) != null;
        } catch (Throwable t) {
            return false;
        }
    }

    public static Typeface get(String assetPath) {
        try {
            if (enabled == -1) enabled = SealGram.pref("font_custom", false) ? 1 : 0;
            if (enabled == 0 || assetPath == null || assetPath.contains("mono")) return null;
            if (!loaded) {
                loaded = true;
                File f = file();
                if (f.isFile()) base = Typeface.createFromFile(f);
            }
            if (base == null) return null;
            boolean bold = assetPath.contains("medium") || assetPath.contains("bold");
            boolean italic = assetPath.contains("italic");
            int style = bold && italic ? Typeface.BOLD_ITALIC : bold ? Typeface.BOLD : italic ? Typeface.ITALIC : Typeface.NORMAL;
            synchronized (styles) {
                if (styles[style] == null) styles[style] = Typeface.create(base, style);
                return styles[style];
            }
        } catch (Throwable t) {
            return null;
        }
    }
}
