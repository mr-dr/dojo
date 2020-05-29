package com.dojo.lit.util

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
}