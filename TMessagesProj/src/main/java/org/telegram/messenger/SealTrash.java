package org.telegram.messenger;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ReplacementSpan;

import androidx.core.graphics.PathParser;

public final class SealTrash {

    private static Path base;

    private SealTrash() {
    }

    private static Path base() {
        if (base == null) {
            base = PathParser.createPathFromPathData("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
        }
        return base;
    }

    public static CharSequence prefix(Paint paint) {
        float w = paint.measureText("\u2007");
        if (w <= 0) w = paint.getTextSize() * 0.5f;
        int n = Math.max(2, (int) Math.ceil(paint.getTextSize() * 1.15f / w));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append('\u2007');
        SpannableString s = new SpannableString(sb.toString());
        s.setSpan(new Span(), 0, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    private static final class Span extends ReplacementSpan {
        private final Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Path path = new Path();
        private final Matrix matrix = new Matrix();

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            return (int) Math.ceil(paint.measureText(text, start, end));
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            try {
                float size = paint.getTextSize() * 0.95f;
                int custom = SealGram.intPref("keep_trash_color", 0);
                int color = custom != 0 ? custom : paint.getColor();
                fill.setColor(color);
                if (custom == 0) fill.setAlpha(paint.getAlpha());
                matrix.setScale(size / 24f, size / 24f);
                matrix.postTranslate(x, y - size * 0.9f);
                path.reset();
                base().transform(matrix, path);
                canvas.drawPath(path, fill);
            } catch (Throwable ignore) {
            }
        }
    }
}
