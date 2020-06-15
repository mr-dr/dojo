package com.dojo.lit

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import android.media.RingtoneManager
import android.media.Ringtone



object Utils {
    fun makeToastLong(messageId: Int) {
        val message = AppController.getInstance().getResources().getString(messageId)
        val toast = Toast.makeText(AppController.getApplicationContext(), message, Toast.LENGTH_LONG)
        toast.show()
    }

    fun makeToastLong(message: String) {
        val toast = Toast.makeText(AppController.getApplicationContext(), message, Toast.LENGTH_LONG)
        toast.show()
    }

    fun makeNotificationSound() {
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(AppController.getApplicationContext(), notification)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }

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

    fun isNetworkAvailable(): Boolean {
        val context = AppController.getInstance()
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }

}