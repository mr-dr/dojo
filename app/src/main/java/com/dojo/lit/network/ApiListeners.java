package com.dojo.lit.network;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public abstract class ApiListeners<T> implements Listener<T>, ErrorListener {

    public void onRequestStarted(ApiRequest<T> req) {

    }

    /**
     * Called on volley background thread
     * @param request
     * @param resString
     * @param response
     */
    public void onDataParsed(ApiRequest<T> request, String resString, T response) {

    }

    public void onResponse(ApiRequest<T> request,  T response) {
        onResponse(response);
    }
}
