package com.dojo.lit.network

import android.text.TextUtils
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.dojo.lit.AppController
import com.dojo.lit.BuildConfig

object RequestManager {
    val APPLICATION_REQ_TAG = "dojoAppRequestTag"
    val TAG_VOLLEY = "dojoVolleyTag"
    val requestQueue = Volley.newRequestQueue(AppController.getApplicationContext())

    fun <T> addToRequestQueue(req: Request<T>?) {
        if (req != null && req.tag == null) {
            req.tag = APPLICATION_REQ_TAG
        }
        if (BuildConfig.DEBUG) {
            Log.d(TAG_VOLLEY, " url req tag : " + req!!.tag)
            logToPostmanCurlRequest(req)
        }
        requestQueue.add(req!!)
    }

    /**
     * Always send screen name as tag
     */
    fun cancelRequests(tag: String) {
        if (!TextUtils.isEmpty(tag)) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG_VOLLEY, "Cancelling requests for tag : $tag")
            }
            requestQueue.cancelAll(RequestQueue.RequestFilter { request -> tag == request.tag })
        }

    }

    private fun logToPostmanCurlRequest(request: Request<*>) {
        val builder = StringBuilder()
        builder.append("POSTMAN curl request: curl ")
        builder.append("-X ")
        when (request.method) {
            Request.Method.POST -> builder.append("POST")
            Request.Method.GET -> builder.append("GET")
            Request.Method.PUT -> builder.append("PUT")
            Request.Method.DELETE -> builder.append("DELETE")
        }
        builder.append(" '")
        builder.append(request.url)
        builder.append("'")

        try {
            for (key in request.headers.keys) {
                builder.append(" \\\n")
                builder.append("    -H '")
                builder.append(key)
                builder.append(": ")
                builder.append(request.headers[key])
                builder.append("'")
            }
            if (request.body != null) {
                builder.append(" \\\n")
                builder.append("-d ")
                var data = String(request.body)
                data = data.replace("\"".toRegex(), "\\\"")
                builder.append("'")
                builder.append(data)
                builder.append("'")
            }
            Log.d("curl", builder.toString())
        } catch (e: AuthFailureError) {
            Log.d("curl","Unable to get body of response or headers for curl logging")
        }

    }
}