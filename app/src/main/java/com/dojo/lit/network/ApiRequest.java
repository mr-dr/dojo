package com.dojo.lit.network;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.dojo.lit.BuildConfig;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by dayanand on 09/03/16.
 */
public class ApiRequest<T> extends Request<T> {

    public static final String NETWORK_TIME_MILLIS = "network_time_millis";
    public static final String TAG_API_RESPONSE = "ApiResponse";

    /**
     * Default charset for JSON request.
     */
    protected static final String PROTOCOL_CHARSET = "utf-8";
    private static final String TAG_VOLLEY = "Volley";
    private final String CONTENT_ENCODING = "Content-Encoding";

    /**
     * Content type for request.
     */
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private Response.Listener<T> mListener;
    private final String mRequestBody;

    private final Type clazzType;
    private final StringBuilder url = new StringBuilder();
    private final StringBuilder method = new StringBuilder();
    private final Map<String, String> headers;
    private final Bundle data = new Bundle();

    private Map<String, String> formData;

    private final Object mLock = new Object();
    private Priority priority;
    private boolean zipRequestBody = false;
    private static String TAG_URL = "dojoLog";

    private final long startTimestamp = System.currentTimeMillis();

    public ApiRequest(Type clazz, int method, String url, Map<String, String> headers, String requestBody,
                      Map<String, String> formData, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.url.delete(0,this.url.length());
        this.url.append(url);
        this.method.delete(0,this.method.length());
        this.method.append(getMethodName(method));
        this.clazzType = clazz;
        this.headers = headers;
        this.mListener = listener;
        this.mRequestBody = requestBody;
        this.formData = formData;
        priority = super.getPriority();
        if (BuildConfig.DEBUG) {
            Log.d(TAG_URL, "url requested : " + url);
            Log.d(TAG_URL, "url requested data : " + requestBody);
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    Log.d(TAG_URL, "url requested header key : " + entry.getKey() + " = " + entry.getValue());
                }
            }
            Log.d(TAG_URL, "url requested FORM data : " + formData);
            if (clazzType == null)
                throw new NullPointerException("Response class type cannot be NULL");
        }
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String json = null;
        try {
            String encoding = response.headers.get("Content-Encoding");
            if (encoding != null && encoding.equals("gzip")) {
                json = parseGzipData(response.data);
            }

            if (TextUtils.isEmpty(json)) {
                json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            }

            T object;
            if (clazzType == String.class) {
                object = (T) json;
            } else {
                object = new Gson().fromJson(json, clazzType);
            }

            Response.Listener<T> listener;
            synchronized (mLock) {
                listener = mListener;
            }
            if (listener instanceof ApiListeners) {
                data.putLong(NETWORK_TIME_MILLIS, response.networkTimeMs);
                ((ApiListeners<T>) listener).onDataParsed(this, json, object);
            }

            if (BuildConfig.DEBUG) {
                String msg = "Api response for url : " + getUrl() + "\nTime : " + response.networkTimeMs + " millis \tData size : " +
                        response.data.length + " byte, " + (String.format(Locale.getDefault(), "%.2f", (float) response.data.length / 1024)) + " kb\nResponse Data : " + json + "/*response ends*/";
                Log.d(TAG_API_RESPONSE, msg);
            }
            return Response.success(object, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            String error = e.getLocalizedMessage();
            String msg = "Error for url : " + getUrl() + "\nerror msg : " + error;
            Exception ex = new Exception(msg);
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
                Log.e(TAG_API_RESPONSE, msg);
                Log.e(TAG_API_RESPONSE, "Error for json url request: response :" + json);
            }

            return Response.error(new ParseError(e));
        }
    }

    public static String parseGzipData(byte[] data) {
        StringBuilder builder = new StringBuilder();
        try {
            final GZIPInputStream gStream = new GZIPInputStream(new ByteArrayInputStream(data));
            final InputStreamReader reader = new InputStreamReader(gStream);
            final BufferedReader in = new BufferedReader(reader);
            String read;
            while ((read = in.readLine()) != null) {
                builder.append(read);
            }
            reader.close();
            in.close();
            gStream.close();

        } catch (IOException e) {

        }
        return builder.toString();
    }

    public static byte[] formGzipData(String payload) {
        if (payload == null)
            return null;
        byte[] data = null;
        ByteArrayOutputStream arr = null;

        try {
            data = payload.getBytes("UTF-8");
            arr = new ByteArrayOutputStream();
            OutputStream zipper = new GZIPOutputStream(arr);
            zipper.write(data);
            zipper.close();
        } catch (IOException e) {
            Log.e(TAG_URL,e.getMessage());
        }
        if (arr == null || data == null) {
            return null;
        } else {
            return arr.toByteArray();
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        long responseTimestamp = System.currentTimeMillis();
        Response.Listener<T> listener;
        synchronized (mLock) {
            listener = mListener;
        }

        if (listener != null) {
            if (listener instanceof ApiListeners) {
                ((ApiListeners<T>) listener).onResponse(this, response);
            } else {
                listener.onResponse(response);
            }


        }
    }

    @Override
    public void deliverError(VolleyError error) {
        long errorTimestamp = System.currentTimeMillis();
        super.deliverError(error);

        if (error != null) {
            String code = error.networkResponse != null ? String.valueOf(error.networkResponse.statusCode) : "";
            String message = "";
            try {
                if (error.networkResponse != null && error.networkResponse.data != null) {

                    String encoding = error.networkResponse.headers.get("Content-Encoding");
                    if (encoding != null && encoding.equals("gzip")) {
                        message = parseGzipData(error.networkResponse.data);
                    }
                    if (TextUtils.isEmpty(message)) {
                        message = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                    }
                }

                if (TextUtils.isEmpty(message)) {
                    message = error.getLocalizedMessage();
                }

                if (TextUtils.isEmpty(message)) {
                    message = error.getClass().getSimpleName();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (BuildConfig.DEBUG) {
                Log.e(TAG_URL, "Error for url request: " + getUrl() + "\n Code : " + code + " , Error Message body : " + message);
            }
        }
    }

    public String getCurlRequest() {
        StringBuilder curlRequest = new StringBuilder();
        curlRequest.delete(0, curlRequest.length());
        curlRequest.append("curl ");
        curlRequest.append("-X ");
        curlRequest.append(method);
        curlRequest.append(" '");
        curlRequest.append(url);
        curlRequest.append("'");

        for (String key : headers.keySet()) {
            curlRequest.append(" \\\n");
            curlRequest.append("    -H '");
            curlRequest.append(key);
            curlRequest.append(": ");
            curlRequest.append(headers.get(key));
            curlRequest.append("'");
        }
        if (mRequestBody != null) {
            curlRequest.append(" \\\n");
            curlRequest.append("-d ");
            String data = zipRequestBody ? Arrays.toString(formGzipData(mRequestBody)) : mRequestBody;
            data = data.replaceAll("\"", "\\\"");
            curlRequest.append("'");
            curlRequest.append(data);
            curlRequest.append("'");
        }
        return curlRequest.toString();
    }

    private String getMethodName(int method) {
        switch (method) {
            case Method.GET:
                return "GET";
            case Method.POST:
                return "POST";
            case Method.DELETE:
                return "DELETE";
            case Method.HEAD:
                return "HEAD";
            case Method.OPTIONS:
                return "OPTIONS";
            case Method.TRACE:
                return "TRACE";
            case Method.PATCH:
                return "PATCH";
        }
        return "GET";
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        try {
            if (formData != null) {
                return super.getBody();
            }
            if (TextUtils.isEmpty(mRequestBody)) {
                return null;
            }
            return zipRequestBody ? formGzipData(mRequestBody) : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
        } catch (AuthFailureError e) {

        }
        return null;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return formData;
    }

    public Bundle getData() {
        return data;
    }

    @Override
    public void cancel() {
        Log.d(TAG_VOLLEY, "Cancelling request : " + getTag() + "\n" + getUrl());
        super.cancel();
        synchronized (mLock) {
            mListener = null;
        }
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Response.Listener<T> getListener() {
        return mListener;
    }

    public void setZipRequestBody(boolean zipRequestBody) {
        this.zipRequestBody = zipRequestBody;
        if(headers != null) {
            if (zipRequestBody) {
                headers.put(CONTENT_ENCODING, "gzip");
            } else {
                headers.remove(CONTENT_ENCODING);
            }
        }
    }
}
