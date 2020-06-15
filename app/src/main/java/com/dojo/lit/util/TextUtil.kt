package com.dojo.lit.util

import java.lang.StringBuilder

object TextUtil {
    val COMMA = ","
    val EQUAL = "="
    val QUESTION_MARK = "?"
    val AMPERSAND = "&"
    val EMPTY = ""
    val NEWLINE = "\n"
    val PIPE_SEPARATOR = " | "

    fun isEmpty(str: String?): Boolean {
        if (str == null || str.trim().isEmpty()) {
            return true
        }
        return false
    }

    fun join(logs: List<String>?, separator: String): String {
        if (logs == null) return EMPTY
        val str = StringBuilder()
        logs.indices.forEach { ind ->
            if (ind > 0) str.append(separator)
            str.append(logs[ind])
        }
        return str.toString()
    }

    fun areSame(str1: String?, str2: String?): Boolean {
        if (str1 == null && str2 == null) return true
        if (str1 == null || str2 == null) return false
        return str1.equals(str2)
    }
}