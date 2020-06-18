package com.dojo.lit.network

import com.dojo.lit.Utils
import com.dojo.lit.util.TextUtil
import com.google.android.gms.common.util.CollectionUtils
import java.lang.StringBuilder

class UrlBuilder {
    lateinit var baseUrl: String
    var path: String? = null
    var params: MutableMap<String, String>? = null

    fun baseUrl(baseUrl: String): UrlBuilder {
        this.baseUrl = baseUrl
        return this
    }

    fun path(path: String): UrlBuilder {
        this.path = path
        return this
    }

    fun params(params: MutableMap<String, String>): UrlBuilder {
        this.params = params
        return this
    }

    fun build(): String {
        if (params == null) params = HashMap()
        params?.put("app_version", Utils.getAppVersionCodeFromPackage().toString()) // fixme move to constants
        if (baseUrl.endsWith("/")) baseUrl = baseUrl.substring(0, baseUrl.length - 1)
        if (!TextUtil.isEmpty(path) && !(path?.startsWith("/") ?: true)) path = "/" + path

        val str = StringBuilder()
        str.append(baseUrl).append(path)
        if (!params.isNullOrEmpty()) {
            str.append(TextUtil.QUESTION_MARK)
            val paramKeys = params?.keys?.toTypedArray()
            paramKeys?.indices?.forEach { keyCount ->
                if(keyCount > 0) str.append(TextUtil.AMPERSAND)
                val key = paramKeys[keyCount]
                val value = params?.get(key)
                str.append(key).append(TextUtil.EQUAL).append(value)
            }
        }
        return str.toString()
    }
}