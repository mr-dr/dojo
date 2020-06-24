package com.dojo.lit.lit.util

import android.view.Gravity
import android.view.Window

object DialogHelper {
    fun alignDialog(window: Window?) {
        val wlp = window?.getAttributes()
        wlp?.gravity = Gravity.TOP
//        wlp?.verticalMargin = resources.getDimension(R.dimen.standard_margin_half)
//        wlp?.flags = wlp?.flags & ~WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window?.setAttributes(wlp)
    }
}
