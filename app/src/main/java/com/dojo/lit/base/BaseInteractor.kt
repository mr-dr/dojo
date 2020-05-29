package com.dojo.lit.base

import androidx.collection.ArraySet
import com.dojo.lit.network.ApiListeners
import com.dojo.lit.network.ApiRequest
import com.dojo.lit.network.RequestManager
import com.dojo.lit.util.TextUtil
import com.google.android.gms.common.util.CollectionUtils
import java.lang.StringBuilder

open class BaseInteractor {
    protected var requestTags: MutableSet<String> = ArraySet(1)
    protected fun <T> startRequest(apiRequest: ApiRequest<T>) {
        startApiRequest<T>(apiRequest)
        addRequestTag(apiRequest.getTag() as String)
    }

    fun <T> startApiRequest(apiRequest: ApiRequest<T>?) {
        if (apiRequest == null) {
            throw IllegalArgumentException("api request can't be null")
        }
//        if (!Utils.isNetworkAvailable()) { // fixme task manager to be impl
//            AppTaskManager.get()
//                .onMain({ apiRequest.errorListener.onErrorResponse(NoConnectionError()) })
//            return
//        }
        if (apiRequest.listener is ApiListeners) {
            (apiRequest.listener as ApiListeners<T>).onRequestStarted(apiRequest)
        }
        RequestManager.addToRequestQueue(apiRequest)
    }

    fun addRequestTag(tag: String) {
        requestTags.add(tag)
    }

    fun cancelRequests() {
        for (tag in requestTags) {
            RequestManager.cancelRequests(tag)
        }

        requestTags.clear()
    }

    protected fun cancelRequestWithTag(tag: String) {
        if (requestTags.contains(tag)) {
            RequestManager.cancelRequests(tag)
            requestTags.remove(tag)
        }
    }

    protected fun convertListToQueryParam(list: List<String>): String {
        if (CollectionUtils.isEmpty(list)) return TextUtil.EMPTY
        val str = StringBuilder()
        list.indices.forEach { index ->
            if (index > 0) str.append(TextUtil.COMMA)
            str.append(list[index])
        }
        return str.toString()
    }
}