package com.dojo.lit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.dojo.lit.R;

public class DojoTextView extends AppCompatTextView {

    boolean hasSheet = false;
    float cornerRadius = 0;
    int bgColor = 0;
    int startColor = 0;
    int midColor = 0;
    int endColor = 0;
    int gradientAngle = -1;
    int strokeColor = 0;
    int gradientType = 0;
    Float strokeWidth = 0F;
    Float strokeDashWidth = 0F;
    Float strokeDashGap = 0F;
    SheetDrawable sheet = null;

    public DojoTextView(Context context) {
        this(context, null);
    }

    public DojoTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DojoTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SheetStyleable,
                defStyleAttr, 0);
        hasSheet = a.getBoolean(R.styleable.SheetStyleable_olSheet, false);
        if (hasSheet) {
            cornerRadius = a.getDimension(R.styleable.SheetStyleable_olSheetCornerRadius, 0);
            bgColor = a.getColor(R.styleable.SheetStyleable_olSheetColor, 0);
            startColor = a.getColor(R.styleable.SheetStyleable_olSheetStartColor, 0);
            midColor = a.getColor(R.styleable.SheetStyleable_olSheetMidColor, 0);
            endColor = a.getColor(R.styleable.SheetStyleable_olSheetEndColor, 0);
            gradientAngle = a.getInt(R.styleable.SheetStyleable_olSheetGradientAngle, 0);
            strokeColor = a.getColor(R.styleable.SheetStyleable_olSheetStrokeColor, 0);
            gradientType = a.getColor(R.styleable.SheetStyleable_olSheetGradientType, 0);
            strokeWidth = a.getDimension(R.styleable.SheetStyleable_olSheetStrokeWidth, 0);
            strokeDashWidth = a.getDimension(R.styleable.SheetStyleable_olSheetStrokeDashWidth, 0);
            strokeDashGap = a.getDimension(R.styleable.SheetStyleable_olSheetStrokeDashGap, 0);
            sheet = SheetHelper.fetchSheetDrawable(cornerRadius, bgColor, startColor, midColor, endColor, gradientAngle, strokeColor, strokeWidth.intValue(), strokeDashWidth, strokeDashGap, gradientType);
            setBackground(sheet);
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        if (hasSheet) {
            sheet.setBgColor(color);
        } else {
            super.setBackgroundColor(color);
        }
    }

    public void setSheetAttrs(float cornerRadius, int bgColor, int startColor, int midColor,
                              int endColor, int angle, int strokeColor, Float strokeWidth,
                              Float strokeDashWidth, Float strokeDashGap, int gradientType) {
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
        this.gradientType = gradientType;
        setBackground(SheetHelper.fetchSheetDrawable(cornerRadius, bgColor, startColor, midColor, endColor, angle, strokeColor, strokeWidth.intValue(), strokeDashWidth, strokeDashGap, gradientType));
    }
}
