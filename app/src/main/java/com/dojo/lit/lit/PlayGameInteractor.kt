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
    fun fetchChanges(listener: ApiListeners<TransactionResponse>) {
        ApiBuilder<TransactionResponse>().get(TransactionResponse::class.java).url(getChangesUrl())
            .tag(getChangesRequestTag()).listener(listener).build()
    }
}