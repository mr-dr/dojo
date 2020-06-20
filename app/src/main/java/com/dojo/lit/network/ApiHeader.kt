package com.dojo.lit.network

import com.dojo.lit.Utils

object ApiHeader {
    // fixme only for testing, UMS logic pending
    val HEADER_KEY = "YVcxaFoybHVaUzV6YjJaMGQyRnlaUT09OnF3MTI="
    fun getDefaultHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
        headers.put("key", HEADER_KEY)
        headers.put("device_id", Utils.getUniqueDeviceId()) // fixme move to constants
        return headers
    }
}