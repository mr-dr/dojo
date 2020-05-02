package com.dojo.lit

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

    fun getString(id:Int): String {
        return AppController.getResources().getString(id)
    }

    fun getString(id: Int, vararg strings: String): String {
        return AppController.getResources().getString(id, strings)
    }
}