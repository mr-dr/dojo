package com.dojo.lit.lit

import com.dojo.lit.base.BaseInteractor
import com.dojo.lit.lit.model.TransactionResponse
import com.dojo.lit.network.ApiBuilder
import com.dojo.lit.network.ApiListeners

/** sample call
 * ApiBuilder<BookingTicket>()
.post(BookingTicket.class)
.url(ApiUrl.captainRaiseIssue())
.body(payload)
.tag(getRequestTag())
.listener(apiListeners)
.build();
 * */

class PlayGameInteractor: BaseInteractor() {
    companion object {
        val GET_CHANGES_TAG = "lit_changes_tag"
    }
    fun fetchChanges(listener: ApiListeners<TransactionResponse>) {
        // TODO add user and game id as headers
        ApiBuilder<TransactionResponse>().get(TransactionResponse::class.java).url(getChangesUrl())
            .tag(GET_CHANGES_TAG).listener(listener).build()
    }

    private fun getChangesUrl(): String {
        return "api.dojo.com/lit/transactions"
    }
}