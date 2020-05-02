package com.dojo.lit.lit.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionResponse(
    @SerializedName("turn_of_player")
    val turnOfPlayer: Int,
    @SerializedName("your_cards")
    val yourCards: List<String>, // expecting sorted cards
    @SerializedName("sets_declared_by_your_team")
    val setsDeclaredByYourTeam: Int,
    @SerializedName("dropped_sets")
    val droppedSets: List<String>,
    @SerializedName("cards_held_by_each_player")
    val cardsHeldByEachPlayer: List<Int>,
    @SerializedName("player_names")
    val playerNames: List<String>,
    @SerializedName("was_last_transaction_successful_drop")
    val wasLastTxnSuccessfulDrop: Boolean,
    @SerializedName("last_transaction")
    val lastTransaction: TransactionData? // null in case of set drop etc
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