package com.dojo.lit.view

import android.graphics.drawable.Drawable

object SheetHelper {

    @JvmStatic
    fun fetchSheetDrawable(
        cornerRadius: Float,
        bgColor: Int,
        strokeColor: Int,
        strokeWidth: Int
    ): SheetDrawable {
        val sheet = SheetDrawable()
        sheet.setRoundedCorners(cornerRadius, cornerRadius, cornerRadius, cornerRadius)
        sheet.setBgColor(bgColor)
        sheet.setStroke(strokeWidth, strokeColor)
        return sheet
    }
}