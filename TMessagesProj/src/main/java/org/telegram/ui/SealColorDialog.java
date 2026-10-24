package org.telegram.ui;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.SealGramI18n;
import org.telegram.messenger.SealTheme;
import org.telegram.ui.ActionBar.AlertDialog;

import java.util.function.IntConsumer;

final class SealColorDialog {

    private static final int[] SWATCHES = {
            0xFFE53935, 0xFFFB8C00, 0xFFFDD835, 0xFF43A047, 0xFF00ACC1, 0xFF2AABEE,
            0xFF8E6BE8, 0xFFD81B60, 0xFF9E9E9E, 0xFF000000, 0xFFFFFFFF, 0xFF795548
    };

    private SealColorDialog() {
    }

    static void show(Activity a, String title, int current, IntConsumer onPicked) {
        if (a == null) return;
        LinearLayout box = new LinearLayout(a);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(AndroidUtilities.dp(20), AndroidUtilities.dp(8), AndroidUtilities.dp(20), 0);
        final EditText input = new EditText(a);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        input.setSingleLine(true);
        input.setText(current != 0 ? SealTheme.hex(current) : "");
        input.setHint(SealGramI18n.t("ThemeHex"));
        box.addView(input);
        LinearLayout row = new LinearLayout(a);
        row.setGravity(Gravity.CENTER);
        LinearLayout row2 = new LinearLayout(a);
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
                        onPicked.accept(SealTheme.parse(input.getText().toString()) | 0xFF000000);
                    } catch (Throwable e) {
                        Toast.makeText(a, SealGramI18n.t("ThemeInvalidColor"), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton(SealGramI18n.t("KeepColorDefault"), (d, w) -> onPicked.accept(0))
                .setNegativeButton(SealGramI18n.t("CommonCancel"), null)
                .show();
    }
}
