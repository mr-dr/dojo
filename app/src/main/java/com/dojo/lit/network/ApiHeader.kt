package com.dojo.lit.network

object ApiHeader {
    // fixme only for testing, UMS logic pending
    val HEADER_KEY = "YVcxaFoybHVaUzV6YjJaMGQyRnlaUT09OnF3MTI="
    fun getDefaultHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
        headers.put("key", HEADER_KEY)
        return headers
    }
}