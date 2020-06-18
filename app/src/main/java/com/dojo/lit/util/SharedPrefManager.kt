package com.dojo.lit.util

import android.content.Context
import com.dojo.lit.AppController
object SharedPrefManager {
    private val filePath = "DojoSharedPref";
    private val sharedPref = AppController.getInstance()
        .getSharedPreferences(filePath, Context.MODE_PRIVATE)
    private val editor = sharedPref.edit();

    val DEVICE_ID_KEY = "device_id"
    val ANDROID_ID_KEY = "device_id"

    fun writeSharedPrefInt(key: String, value: Int) {
        editor.putInt(key, value);
        editor.commit();
    }

    fun writeSharedPrefBool(key: String, value: Boolean) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    fun writeSharedPrefFloat(key: String, value: Float) {
        editor.putFloat(key, value);
        editor.commit();
    }

    fun writeSharedPrefLong(key: String, value: Long) {
        editor.putLong(key, value);
        editor.commit();
    }

    fun writeSharedPrefString(key: String, value: String) {
        editor.putString(key, value);
        editor.commit();
    }

    fun readSharedPrefInt(key: String, defaultValue: Int): Int {
        return sharedPref.getInt(key, defaultValue)
    }

    fun readSharedPrefBool(key: String, defaultValue: Boolean): Boolean {
        return sharedPref.getBoolean(key, defaultValue)
    }

    fun readSharedPrefFloat(key: String, defaultValue: Float): Float {
        return sharedPref.getFloat(key, defaultValue)
    }

    fun readSharedPrefLong(key: String, defaultValue: Long): Long {
        return sharedPref.getLong(key, defaultValue)
    }

    fun readSharedPrefString(key: String, defaultValue: String): String? {
        return sharedPref.getString(key, defaultValue)
    }

}

