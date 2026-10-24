package org.telegram.messenger;

import android.graphics.Color;

import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeColors;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

public final class SealTheme {

    private SealTheme() {
    }

    public static int accent() {
        return SealGram.intPref("theme_accent", 0xFF8E6BE8);
    }

    public static int background() {
        return SealGram.intPref("theme_bg", 0xFF121212);
    }

    public static int bubble() {
        return SealGram.intPref("theme_bubble", 0xFF5B3FC4);
    }

    public static int parse(String s) {
        s = s.trim();
        if (!s.startsWith("#")) s = "#" + s;
        return Color.parseColor(s);
    }

    public static String hex(int c) {
        return String.format("#%06X", c & 0xFFFFFF);
    }

    private static boolean dark(int c) {
        return (0.299 * Color.red(c) + 0.587 * Color.green(c) + 0.114 * Color.blue(c)) < 140;
    }

    private static int mix(int a, int b, float t) {
        return Color.rgb(
                (int) (Color.red(a) * (1 - t) + Color.red(b) * t),
                (int) (Color.green(a) * (1 - t) + Color.green(b) * t),
                (int) (Color.blue(a) * (1 - t) + Color.blue(b) * t));
    }

    public static boolean apply() {
        try {
            int acc = accent() | 0xFF000000;
            int bg = background() | 0xFF000000;
            int bub = bubble() | 0xFF000000;
            boolean isDark = dark(bg);
            int text = isDark ? 0xFFFFFFFF : 0xFF000000;
            int gray = mix(bg, text, 0.55f);
            int onAcc = dark(acc) ? 0xFFFFFFFF : 0xFF000000;
            int onBub = dark(bub) ? 0xFFFFFFFF : 0xFF000000;
            int gray2 = mix(bg, text, isDark ? 0.08f : 0.06f);
            int divider = mix(bg, text, 0.14f);
            int link = isDark ? mix(acc, 0xFFFFFFFF, 0.25f) : acc;
            HashMap<String, Integer> o = new HashMap<>();
            o.put("windowBackgroundWhite", bg);
            o.put("windowBackgroundGray", mix(bg, isDark ? 0xFF000000 : 0xFF808080, 0.25f));
            o.put("windowBackgroundWhiteBlackText", text);
            o.put("windowBackgroundWhiteGrayText", gray);
            o.put("windowBackgroundWhiteGrayText2", gray);
            o.put("windowBackgroundWhiteGrayText3", gray);
            o.put("windowBackgroundWhiteGrayText4", gray);
            o.put("windowBackgroundWhiteGrayIcon", gray);
            o.put("windowBackgroundWhiteBlueHeader", link);
            o.put("windowBackgroundWhiteBlueText", link);
            o.put("windowBackgroundWhiteBlueText4", link);
            o.put("windowBackgroundWhiteBlueIcon", link);
            o.put("windowBackgroundWhiteBlueButton", link);
            o.put("windowBackgroundWhiteLinkText", link);
            o.put("actionBarDefault", acc);
            o.put("actionBarDefaultTitle", onAcc);
            o.put("actionBarDefaultIcon", onAcc);
            o.put("actionBarDefaultSubtitle", onAcc);
            o.put("actionBarDefaultSelector", (onAcc & 0xFFFFFF) | 0x33000000);
            o.put("divider", divider);
            o.put("dialogBackground", bg);
            o.put("dialogBackgroundGray", gray2);
            o.put("dialogTextBlack", text);
            o.put("dialogTextGray", gray);
            o.put("dialogTextGray2", gray);
            o.put("dialogTextGray3", gray);
            o.put("dialogTextGray4", gray);
            o.put("dialogTextBlue", link);
            o.put("dialogTextBlue2", link);
            o.put("dialogButton", link);
            o.put("chats_name", text);
            o.put("chats_message", gray);
            o.put("chats_nameMessage", link);
            o.put("chats_date", gray);
            o.put("chats_actionBackground", acc);
            o.put("chats_actionIcon", onAcc);
            o.put("chats_unreadCounter", acc);
            o.put("chats_unreadCounterText", onAcc);
            o.put("chats_menuBackground", bg);
            o.put("chats_menuItemText", text);
            o.put("chat_wallpaper", mix(bg, isDark ? 0xFF000000 : 0xFF808080, 0.15f));
            o.put("chat_outBubble", bub);
            o.put("chat_outBubbleSelected", mix(bub, text, 0.12f));
            o.put("chat_messageTextOut", onBub);
            o.put("chat_inBubble", gray2 == bg ? mix(bg, text, 0.1f) : mix(bg, text, 0.1f));
            o.put("chat_inBubbleSelected", mix(bg, text, 0.16f));
            o.put("chat_messageTextIn", text);
            o.put("chat_messagePanelBackground", bg);
            o.put("chat_messagePanelText", text);
            o.put("chat_messagePanelHint", gray);
            o.put("chat_messagePanelIcons", gray);
            o.put("chat_messagePanelSend", acc);
            o.put("chat_inTimeText", gray);
            o.put("chat_outTimeText", mix(bub, onBub, 0.7f));
            o.put("chat_messageLinkIn", link);
            o.put("chat_messageLinkOut", onBub);
            o.put("listSelectorSDK21", (text & 0xFFFFFF) | 0x14000000);

            int[] def = Theme.getDefaultColors();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < def.length; i++) {
                String name = ThemeColors.getStringName(i);
                if (name == null) continue;
                Integer v = o.get(name);
                sb.append(name).append('=').append(v != null ? v : def[i]).append('\n');
            }
            File f = new File(ApplicationLoader.getFilesDirFixed(), "sealgram_custom_theme.attheme");
            try (FileOutputStream out = new FileOutputStream(f)) {
                out.write(sb.toString().getBytes("UTF-8"));
            }
            Theme.applyThemeFile(f, "SealGram theme", null, false);
            return true;
        } catch (Throwable t) {
            FileLog.e(t);
            return false;
        }
    }
}
