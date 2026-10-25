package org.telegram.ui.Components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

@SuppressLint("ViewConstructor")
public class SealHiddenView extends View {

    public SealHiddenView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), 1);
    }
}
