package com.dojo.lit

import android.graphics.drawable.Drawable
import android.widget.Toast

object Utils {
    fun makeToastLong(messageId: Int) {
        val message = AppController.getResources().getString(messageId)
        val toast = Toast.makeText(AppController.applicationContext, message, Toast.LENGTH_LONG)
        toast.show()
    }

    fun makeToastLong(message: String) {
        val toast = Toast.makeText(AppController.applicationContext, message, Toast.LENGTH_LONG)
        toast.show()
    }

    fun getColor(id:Int): Int {
        return AppController.getResources().getColor(id)
    }

    fun getDimen(id:Int): Float {
        return AppController.getResources().getDimension(id)
    }

    fun getString(id:Int): String {
        return AppController.getResources().getString(id)
    }

    fun getString(id: Int, vararg strings: String): String {
        return AppController.getResources().getString(id, strings)
    }

}