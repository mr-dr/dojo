package com.dojo.lit.util

import com.google.gson.Gson

object GsonUtil {
    var gson: Gson? = null

    private fun gson(): Gson {
        if(gson == null) {
            synchronized(GsonUtil::class.java) {
                if(gson == null) {
                    gson = Gson()
                }
            }
        }
        return gson!!
    }

    fun toJson(obj: Any?): String {
        if(obj == null) return TextUtil.EMPTY
        return gson().toJson(obj)
    }

    fun <T> fromJson(json: String, clazz: Class<T>): T {
        return gson().fromJson(json, clazz)
    }
}