package com.dojo.lit.view

import android.content.ClipDescription
import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.dojo.lit.Utils
import android.util.Log
import com.dojo.lit.util.GsonUtil

class DroppableLinearLayout(context: Context?, attr: AttributeSet) : LinearLayout(context, attr), Droppable {

    var listener: OnDragListener? = null

    override fun setup() {
        setOnDragListener(listener ?: defaultListener)
    }

    override fun setDropListener(listener: OnDragListener) {
        this.listener = listener
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    val defaultListener = OnDragListener { v, event ->
        when(event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                v.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> true
            DragEvent.ACTION_DRAG_EXITED -> {
                v.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                val item = event.clipData.getItemAt(0)
                val dragData = item.text
                v.invalidate()
                val draggedView = event.localState as View
                val parent = draggedView.parent as ViewGroup
                parent.removeView(draggedView)
                val destination = v as DroppableLinearLayout
                destination.addView(draggedView)
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                val draggedView = event.localState as View
                draggedView.visibility = View.VISIBLE
                v.invalidate()
                true
            }
            else -> false
        }
    }
}