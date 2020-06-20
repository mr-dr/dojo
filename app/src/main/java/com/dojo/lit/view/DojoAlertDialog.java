package com.dojo.lit.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.dojo.lit.R;

public class DojoAlertDialog extends Dialog implements View.OnClickListener{

    public Context context;
    public Dialog d;
    private DojoTextView mPositive, mNegative, mTitle;
    private LinearLayout mCustomLL;
    private DojoLinearLayout mRootView;

    private Runnable positiveRunnable, negativeRunnable;
    private String title, positiveBtn, negativeBtn;
    private View layout;

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

    public DojoAlertDialog(Context context, String title, String positiveBtn, String negativeBtn,
                           Runnable positiveRunnable, Runnable negativeRunnable, View layout) {
        super(context);
        this.context = context;
        this.positiveRunnable = positiveRunnable;
        this.negativeRunnable = negativeRunnable;
        this.title = title;
        this.positiveBtn = positiveBtn;
        this.negativeBtn = negativeBtn;
        this.layout = layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dojo_alert_dialog);
        mRootView = findViewById(R.id.root_view);
        mTitle = findViewById(R.id.title);
        mCustomLL = findViewById(R.id.custom_ll);
        mPositive = findViewById(R.id.positive_button);
        mNegative = findViewById(R.id.negative_button);
        mPositive.setOnClickListener(this);
        mNegative.setOnClickListener(this);
        mTitle.setText(title);
        mPositive.setText(positiveBtn);
        mNegative.setText(negativeBtn);
        mCustomLL.addView(layout);
        setBackground();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positive_button:
                positiveRunnable.run();
                break;
            case R.id.negative_button:
                negativeRunnable.run();
                dismiss();
                break;
            default:
                break;
        }
    }

    private void setBackground() {
        if (!hasSheet) return;
        setBackground(SheetHelper.fetchSheetDrawable(cornerRadius, bgColor, startColor, midColor,
                endColor, gradientAngle, strokeColor, strokeWidth.intValue(), strokeDashWidth, strokeDashGap, gradientType));
    }

    private void setBackground(@NonNull SheetDrawable sheet) {
//        if (mRootView != null) mRootView.setBackground(sheet);
        Window window = getWindow();
        if(window != null) {
            window.getDecorView().setBackground(sheet);
        }
    }

    public void setSheetAttrs(float cornerRadius, int bgColor, int startColor, int midColor,
                              int endColor, int angle, int strokeColor, Float strokeWidth, Float strokeDashWidth, Float strokeDashGap, int gradientType) {
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