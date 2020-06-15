package com.dojo.lit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class DojoLinearLayout extends LinearLayout {

    boolean hasSheet = false;
    float cornerRadius = 0;
    int bgColor = 0;
    int startColor = 0;
    int midColor = 0;
    int endColor = 0;
    int gradientAngle = -1;
    int strokeColor = 0;
    Float strokeWidth = 0F;
    Float strokeDashWidth = 0F;
    Float strokeDashGap = 0F;
    SheetDrawable sheet = null;

    public DojoLinearLayout(Context context) {
        this(context, null);
    }

    public DojoLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DojoLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DojoLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setSheetAttrs(float cornerRadius, int bgColor, int startColor, int midColor,
                              int endColor, int angle, int strokeColor, Float strokeWidth, Float strokeDashWidth, Float strokeDashGap) {
        hasSheet = true;
        this.cornerRadius = cornerRadius;
        this.bgColor = bgColor;
        this.startColor = startColor;
        this.midColor = midColor;
        this.endColor = endColor;
        this.gradientAngle = angle;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        this.strokeDashWidth = strokeDashWidth;
        this.strokeDashGap = strokeDashGap;
        setBackground(SheetHelper.fetchSheetDrawable(cornerRadius, bgColor, startColor, midColor, endColor, angle, strokeColor, strokeWidth.intValue(), strokeDashWidth, strokeDashGap));
    }
}
