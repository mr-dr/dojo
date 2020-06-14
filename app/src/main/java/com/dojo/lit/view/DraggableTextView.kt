package com.dojo.lit.view

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.core.view.DragStartHelper

class DraggableTextView(context: Context?, attrs: AttributeSet?, val txt: String, val id: String?) : DojoTextView(context, attrs), Draggable {

    override fun getIdentifier(): String {
        return id ?: text.toString()
    }


    override fun setup() {
        text = txt
        apply { // move to Draggable interface
            setOnLongClickListener {
                val cliptext = "This is clipData text"
                val item = ClipData.Item(cliptext)
                val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
                val dragData = ClipData(cliptext, mimeTypes, item)

                val dragShadowBuilder = View.DragShadowBuilder(it)

                it.startDragAndDrop(dragData, dragShadowBuilder, it, 0)
                it.visibility = View.INVISIBLE

                true
            }
        }
    }
}