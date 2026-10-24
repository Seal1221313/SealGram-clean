package org.telegram.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;
import org.telegram.messenger.SealFont;
import org.telegram.messenger.SealGram;
import org.telegram.messenger.SealGramI18n;
import org.telegram.messenger.SealTheme;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;

import java.io.FileOutputStream;
import java.io.InputStream;

public class SealGramStyleActivity extends BaseFragment {

    private static final int REQ_FONT = 7311;
    private static final int[] SWATCHES = {
            0xFF8E6BE8, 0xFF2AABEE, 0xFF1E88E5, 0xFF00ACC1, 0xFF43A047, 0xFFFDD835,
            0xFFFB8C00, 0xFFE53935, 0xFFD81B60, 0xFF121212, 0xFF1E1E2E, 0xFFFFFFFF
    };

    private LinearLayout root;
    private Context ctx;

    @Override
    public View createView(Context context) {
        ctx = context;
        SealGramHubActivity.setupBar(actionBar, SealGramI18n.t("StyleTitle"), this::finishFragment);
        root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(0, AndroidUtilities.dp(12), 0, AndroidUtilities.dp(24));
        render();
        fragmentView = SealGramHubActivity.scroll(context, root);
        return fragmentView;
    }

    private void render() {
        root.removeAllViews();
        SealGramHubActivity.header(ctx, root, SealGramI18n.t("FontHeader"));
        LinearLayout f = SealGramHubActivity.card(ctx, root);
        SealGramHubActivity.toggle(ctx, f, SealGramI18n.t("FontUse"), SealGramI18n.t("FontUseDesc"), SealGram.pref("font_custom", false), v -> SealGram.setPref("font_custom", v));
        SealGramHubActivity.row(ctx, f, R.drawable.msg_text_outlined, SealGramI18n.t("FontPick"), null, () -> {
            try {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("*/*");
                i.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(i, REQ_FONT);
            } catch (Throwable t) {
                toast(SealGramI18n.t("FontInvalid"));
            }
        });
        SealGramHubActivity.row(ctx, f, R.drawable.msg_delete, SealGramI18n.t("FontReset"), null, () -> {
            SealFont.file().delete();
            SealGram.setPref("font_custom", false);
            toast(SealGramI18n.t("FontApplied"));
            render();
        });

        SealGramHubActivity.header(ctx, root, SealGramI18n.t("ThemeHeader"));
        LinearLayout t = SealGramHubActivity.card(ctx, root);
        colorRow(t, "ThemeAccent", "theme_accent", SealTheme.accent());
        colorRow(t, "ThemeBackground", "theme_bg", SealTheme.background());
        colorRow(t, "ThemeBubble", "theme_bubble", SealTheme.bubble());
        SealGramHubActivity.row(ctx, t, R.drawable.msg_palette, SealGramI18n.t("ThemeApply"), null, () -> {
            toast(SealTheme.apply() ? SealGramI18n.t("ThemeApplied") : SealGramI18n.t("ThemeFailed"));
        });
        SealGramHubActivity.note(ctx, root, SealGramI18n.t("ThemeNote"));
    }

    private void colorRow(LinearLayout card, String titleKey, String prefKey, int current) {
        SealGramHubActivity.row(ctx, card, R.drawable.msg_palette, SealGramI18n.t(titleKey), SealTheme.hex(current), () -> pickColor(SealGramI18n.t(titleKey), prefKey, current));
    }

    private void pickColor(String title, String prefKey, int current) {
        Activity a = getParentActivity();
        if (a == null) return;
        LinearLayout box = new LinearLayout(a);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(AndroidUtilities.dp(20), AndroidUtilities.dp(8), AndroidUtilities.dp(20), 0);
        final EditText input = new EditText(a);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        input.setSingleLine(true);
        input.setText(SealTheme.hex(current));
        input.setHint(SealGramI18n.t("ThemeHex"));
        box.addView(input);
        LinearLayout row = new LinearLayout(a);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);
        LinearLayout row2 = new LinearLayout(a);
        row2.setOrientation(LinearLayout.HORIZONTAL);
        row2.setGravity(Gravity.CENTER);
        for (int i = 0; i < SWATCHES.length; i++) {
            final int c = SWATCHES[i];
            View v = new View(a);
            GradientDrawable g = new GradientDrawable();
            g.setShape(GradientDrawable.OVAL);
            g.setColor(c);
            g.setStroke(AndroidUtilities.dp(1), 0x66888888);
            v.setBackground(g);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(AndroidUtilities.dp(36), AndroidUtilities.dp(36));
            lp.setMargins(AndroidUtilities.dp(4), AndroidUtilities.dp(10), AndroidUtilities.dp(4), 0);
            v.setOnClickListener(x -> input.setText(SealTheme.hex(c)));
            (i < 6 ? row : row2).addView(v, lp);
        }
        box.addView(row);
        box.addView(row2);
        new AlertDialog.Builder(a)
                .setTitle(title)
                .setView(box)
                .setPositiveButton(SealGramI18n.t("CommonSave"), (d, w) -> {
                    try {
                        int c = SealTheme.parse(input.getText().toString());
                        SealGram.setIntPref(prefKey, c | 0xFF000000);
                        render();
                    } catch (Throwable e) {
                        toast(SealGramI18n.t("ThemeInvalidColor"));
                    }
                })
                .setNegativeButton(SealGramI18n.t("CommonCancel"), null)
                .show();
    }

    private void toast(String s) {
        try {
            Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
        } catch (Throwable ignore) {
        }
    }

    @Override
    public void onActivityResultFragment(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQ_FONT || resultCode != Activity.RESULT_OK || data == null || data.getData() == null) return;
        try (InputStream in = getParentActivity().getContentResolver().openInputStream(data.getData());
             FileOutputStream out = new FileOutputStream(SealFont.file())) {
            byte[] buf = new byte[16384];
            int n;
            while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
        } catch (Throwable t) {
            toast(SealGramI18n.t("FontInvalid"));
            return;
        }
        if (SealFont.valid(SealFont.file())) {
            SealGram.setPref("font_custom", true);
            toast(SealGramI18n.t("FontApplied"));
            render();
        } else {
            SealFont.file().delete();
            toast(SealGramI18n.t("FontInvalid"));
        }
    }
}
