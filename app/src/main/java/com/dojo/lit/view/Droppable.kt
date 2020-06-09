package com.dojo.lit.view

import android.view.View

interface Droppable {
    fun setup()
    fun setDropListener(listener: View.OnDragListener)
}
