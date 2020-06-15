package com.dojo.lit.view

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable

object SheetHelper {

    @JvmStatic
    fun fetchSheetDrawable(
        cornerRadius: Float,
        bgColor: Int,
        startColor: Int,
        midColor: Int, // fixme doesn't consider mid color
        endColor: Int,
        gradientAngle: Int,
        strokeColor: Int,
        strokeWidth: Int,
        strokeDashWidth: Float,
        strokeDashGap: Float
    ): SheetDrawable {
        val sheet = SheetDrawable()
        sheet.setRoundedCorners(cornerRadius, cornerRadius, cornerRadius, cornerRadius)
        if (startColor == 0) {
            if (bgColor != 0) {
                sheet.setBgColor(bgColor)
            }
        } else {
            val orientation = getOrientation(gradientAngle)
            val colors = intArrayOf(startColor, endColor)
            sheet.setBgGradient(colors, orientation)
        }
        sheet.setStroke(strokeWidth, strokeColor, strokeDashWidth, strokeDashGap)
        return sheet
    }

    private fun getOrientation(gradientAngle: Int): GradientDrawable.Orientation {
        when (gradientAngle) {
            1 -> return GradientDrawable.Orientation.RIGHT_LEFT
            2 -> return GradientDrawable.Orientation.TOP_BOTTOM
            3 -> return GradientDrawable.Orientation.BOTTOM_TOP
            4 -> return GradientDrawable.Orientation.TL_BR
            5 -> return GradientDrawable.Orientation.BR_TL
            6 -> return GradientDrawable.Orientation.TR_BL
            7 -> return GradientDrawable.Orientation.BL_TR
            else -> return GradientDrawable.Orientation.LEFT_RIGHT // case 0 & default
        }
    }
}