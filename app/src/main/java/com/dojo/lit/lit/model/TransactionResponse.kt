package com.dojo.lit.lit.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionResponse(
    @SerializedName("turn")
    val turnOfPlayer: Int,
    @SerializedName("dropped_sets")
    val droppedSets: String, // List<String>, fixme
    @SerializedName("last_transaction_drop")
    val wasLastTxnSuccessfulDrop: String, // fixme - backend
    @SerializedName("score")
    val scoreboardText: String, // fixme - backend
    @SerializedName("logs")
    val logsCount: String, // fixme - backend not string
    @SerializedName("1")
    val player1: PlayerInfo,
    @SerializedName("2")
    val player2: PlayerInfo,
    @SerializedName("3")
    val player3: PlayerInfo,
    @SerializedName("4")
    val player4: PlayerInfo,
    @SerializedName("5")
    val player5: PlayerInfo,
    @SerializedName("6")
    val player6: PlayerInfo
//    @SerializedName("last_transaction")
//    val lastTransaction: TransactionData? // null in case of set drop etc
) : Parcelable

@Parcelize
data class PlayerInfo(
    @SerializedName("alias")
    val name: String,
    @SerializedName("cards") // fixme - card info should not be accessible for other players
    val cards: List<String>
) : Parcelable

@Parcelize
data class TransactionData(
    @SerializedName("asked_by")
    val askedBy: Int,
    @SerializedName("asked_from")
    val askedFrom: Int,
    @SerializedName("asked_for")
    val askedFor: String,
    @SerializedName("was_successful")
    val wasSuccessful: Boolean
) : Parcelable