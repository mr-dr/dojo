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
    int strokeColor = 0;
    Float strokeWidth = 0F;
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
                R.styleable.DojoTextView,
                defStyleAttr, 0);
        hasSheet = a.getBoolean(R.styleable.DojoTextView_olSheet, false);
        if (hasSheet) {
            cornerRadius = a.getDimension(R.styleable.DojoTextView_olSheetCornerRadius, 0);
            bgColor = a.getColor(R.styleable.DojoTextView_olSheetColor, 0);
            strokeColor = a.getColor(R.styleable.DojoTextView_olSheetStrokeColor, 0);
            strokeWidth = a.getDimension(R.styleable.DojoTextView_olSheetStrokeWidth, 0);
            sheet = SheetHelper.fetchSheetDrawable(cornerRadius, bgColor, strokeColor, strokeWidth.intValue());
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

    public void setSheetAttrs(float cornerRadius, int bgColor, int strokeColor, Float strokeWidth) {
        hasSheet = true;
        this.cornerRadius = cornerRadius;
        this.bgColor = bgColor;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        setBackground(SheetHelper.fetchSheetDrawable(cornerRadius, bgColor, strokeColor, strokeWidth.intValue()));
    }
}
