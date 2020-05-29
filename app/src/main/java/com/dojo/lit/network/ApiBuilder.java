package com.dojo.lit.network;

import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.dojo.lit.lit.model.CreateRoomResponse;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dayanand on 28/02/18.
 */
public class ApiBuilder<T> {

    int method;
    String url;
    Map<String, String> headers;
    String requestBody;
    ApiListeners<T> listeners;
    private String tag;
    private RetryPolicy retryPolicy;
    private Class<?> tClass;
    private boolean asList;
    private Type type;
    private Map<String, String> formData;

    public ApiBuilder<T> get(Class<?> tClass) {
        initWithMethod(Request.Method.GET, tClass);
        return this;
    }

    public ApiBuilder<T> getAsList(Class<?> tClass) {
        initWithMethod(Request.Method.GET, tClass);
        this.asList = true;
        return this;
    }

    public ApiBuilder<T> put(Class<?> tClass) {
        initWithMethod(Request.Method.PUT, tClass);
        return this;
    }

    public ApiBuilder<T> post(Class<?> tClass) {
        initWithMethod(Request.Method.POST, tClass);
        return this;
    }

    public ApiBuilder<T> patch(Class<?> tClass) {
        initWithMethod(Request.Method.PATCH, tClass);
        return this;
    }

    public ApiBuilder<T> delete(Class<?> tClass) {
        initWithMethod(Request.Method.DELETE, tClass);
        return this;
    }

    private ApiBuilder<T> initWithMethod(int requestMethod, Class<?> tClass) {
        this.tClass = tClass;
        this.method = requestMethod;
        return this;
    }

    public ApiBuilder<T> url(String url) {
        this.url = url;
        return this;
    }

    public ApiBuilder<T> tag(String tag) {
        this.tag = tag;
        return this;
    }

    public ApiBuilder<T> body(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public ApiBuilder<T> listener(ApiListeners<T> apiListeners) {
        this.listeners = apiListeners;
        return this;
    }

    public ApiBuilder<T> headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public ApiBuilder<T> retryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
        return this;
    }

    public ApiRequest<T> build() {
        if (TextUtils.isEmpty(url) || listeners == null) {
            throw new IllegalArgumentException("url/listeners required");
        }
        updateHeaders();
        ApiRequest<T> apiRequest = new ApiRequest<>(getType(), method, url, headers, requestBody, formData, listeners, listeners);
        apiRequest.setTag(tag);

        if (retryPolicy == null) {
            apiRequest.setRetryPolicy(
                    new DefaultRetryPolicy(0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            apiRequest.setRetryPolicy(retryPolicy);
        }

        return apiRequest;
    }

    private Type getType() {
        return type != null ? type : (asList ? TypeToken.getParameterized(List.class, tClass).getType() : tClass);
    }

    private void updateHeaders() {
        if (headers == null) {
            headers = getAccessHeader();
        }
    }

    // can be moved in future
    private Map<String, String> getAccessHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("access_token","rand"); // fixme
//        headers.put("game_id",UserInfo.getOngoingGameId());
        return headers;
    }

    public ApiBuilder<T> method(int method) {
        this.method = method;
        return this;
    }

    public ApiBuilder<T> responseType(Type type) {
        this.type = type;
        return this;
    }

    public ApiBuilder<T> formData(Map<String, String> formData) {
        this.formData = formData;
        return this;
    }
}


