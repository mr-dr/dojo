package com.dojo.lit.lit

import com.dojo.lit.base.BaseInteractor
import com.dojo.lit.lit.model.CreateRoomResponse
import com.dojo.lit.lit.model.DropSetData
import com.dojo.lit.lit.model.TransactionData
import com.dojo.lit.lit.model.TransactionResponse
import com.dojo.lit.network.*

class GameInteractor: BaseInteractor() {
    companion object {
        val BASE_URL = "https://us-central1-litt-276414.cloudfunctions.net" // fixed for Firebase Cloud Functions
        val CREATE_ROOM_TAG = "createRoomRequestTag"
        val JOIN_ROOM_TAG = "joinRoomRequestTag"
        val LEAVE_ROOM_TAG = "leaveRoomRequestTag"
        val ASK_CARD_TAG = "askCardRequestTag"
        val DROP_SET_TAG = "dropSetRequestTag"
        val TRANSFER_TURN_TAG = "transferRequestTag"
        val GET_CHANGES_TAG = "lit_changes_tag"
    }

    fun createRoom(logsSize:Int, listener: ApiListeners<CreateRoomResponse>) {
        val params = HashMap<String, String>()
        params.put(ApiParams.LOGS, logsSize.toString())
        val url = UrlBuilder().baseUrl(BASE_URL).path(LitUrlPaths.CREATE_ROOM).params(params).build()
        val request = ApiBuilder<CreateRoomResponse>()
            .get(CreateRoomResponse::class.java)
            .url(url)
            .headers(ApiHeader.getDefaultHeaders())
            .listener(listener)
            .tag(CREATE_ROOM_TAG)
            .build()
        startApiRequest(request)
    }

    // need entire transaction response
    fun joinRoom(alias: String, gameId: String, playerNo: Int, listener: ApiListeners<String>) {
        val params = HashMap<String, String>()
        params.put(ApiParams.ALIAS, alias)
        params.put(ApiParams.GAME_ID, gameId)
        params.put(ApiParams.PLAYER_NO, playerNo.toString())
        val url = UrlBuilder().baseUrl(BASE_URL).path(LitUrlPaths.JOIN_ROOM).params(params).build()
        val request = ApiBuilder<String>()
            .get(String::class.java)
            .url(url)
            .headers(ApiHeader.getDefaultHeaders())
            .listener(listener)
            .tag(JOIN_ROOM_TAG)
            .build()
        startApiRequest(request)
    }

    fun leaveRoom(gameId: String, playerNo: Int, listener: ApiListeners<String>) {
        val params = HashMap<String, String>()
        params.put(ApiParams.GAME_ID, gameId)
        params.put(ApiParams.PLAYER_NO, playerNo.toString())
        val url = UrlBuilder().baseUrl(BASE_URL).path(LitUrlPaths.LEAVE_ROOM).params(params).build()
        val request = ApiBuilder<String>()
            .get(String::class.java)
            .url(url)
            .headers(ApiHeader.getDefaultHeaders())
            .listener(listener)
            .tag(LEAVE_ROOM_TAG)
            .build()
        startApiRequest(request)
    }

    // only success & failure matter here
    fun askCard(gameId: String, transactionData: TransactionData, listener: ApiListeners<String>) {
        val params = HashMap<String, String>()
        params.put(ApiParams.PLAYER_A, transactionData.askedBy.toString()) // fixme should always be for your user
        params.put(ApiParams.PLAYER_B, transactionData.askedFrom.toString())
        params.put(ApiParams.CARD, transactionData.askedFor)
        params.put(ApiParams.GAME_ID, gameId)
        val url = UrlBuilder().baseUrl(BASE_URL).path(LitUrlPaths.ASK_CARD).params(params).build()
        val request = ApiBuilder<String>()
            .get(String::class.java)
            .url(url)
            .headers(ApiHeader.getDefaultHeaders())
            .listener(listener)
            .tag(ASK_CARD_TAG)
            .build()
        startApiRequest(request)
    }


    // only success & failure matter here
    fun dropSet(gameId: String, data: DropSetData, listener: ApiListeners<String>) {
        val params = HashMap<String, String>()
        params.put(ApiParams.PLAYER_A, data.playerA.toString()) // fixme should always be for your user
        params.put(ApiParams.PLAYER_B, data.playerB.toString())
        params.put(ApiParams.PLAYER_C, data.playerC.toString())
        if (!data.cardsA.isEmpty()) params.put(
            ApiParams.CARDS_A,
            convertListToQueryParam(data.cardsA)
        )
        if (!data.cardsB.isEmpty()) params.put(
            ApiParams.CARDS_B,
            convertListToQueryParam(data.cardsB)
        )
        if (!data.cardsC.isEmpty()) params.put(
            ApiParams.CARDS_C,
            convertListToQueryParam(data.cardsC)
        )
        params.put(ApiParams.GAME_ID, gameId)
        val url = UrlBuilder().baseUrl(BASE_URL).path(LitUrlPaths.DROP_SET).params(params).build()
        val request = ApiBuilder<String>()
            .get(String::class.java)
            .url(url)
            .headers(ApiHeader.getDefaultHeaders())
            .listener(listener)
            .tag(DROP_SET_TAG)
            .build()
        startApiRequest(request)
    }

    // only success & failure matter here
    fun transferTurn(playerA:Int, playerB: Int, gameId: String, listener: ApiListeners<String>) {
        // fixme - logic needed on client that only valid transfers are initiated
        val params = HashMap<String, String>()
        params.put(ApiParams.PLAYER_A, playerA.toString())
        params.put(ApiParams.PLAYER_B, playerB.toString())
        params.put(ApiParams.GAME_ID, gameId)
        val url = UrlBuilder().baseUrl(BASE_URL).path(LitUrlPaths.TRANSFER_TURN).params(params).build()
        val request = ApiBuilder<String>()
            .get(String::class.java)
            .url(url)
            .headers(ApiHeader.getDefaultHeaders())
            .listener(listener)
            .tag(TRANSFER_TURN_TAG)
            .build()
        startApiRequest(request)
    }

    // TODO below func not necessary with realtime DB, will be used on migration
    fun fetchChanges(listener: ApiListeners<TransactionResponse>) {
        // TODO add user and game id as headers
        ApiBuilder<TransactionResponse>().get(TransactionResponse::class.java).url(
            getChangesUrl()
        )
            .tag(GET_CHANGES_TAG).listener(listener).build()
    }

    private fun getChangesUrl(): String {
        return "api.dojo.com/lit/transactions"
    }
}