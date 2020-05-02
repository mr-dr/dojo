package com.dojo.lit.lit.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class  TransactionResponse(
    @SerializedName("turn_of_player")
    val turnOfPlayer: Int,
    @SerializedName("your_cards")
    val yourCards: List<String>,
    @SerializedName("sets_declared_by_your_team")
    val setsDeclaredByYourTeam: Int,
    @SerializedName("declared_sets")
    val declaredSets: List<String>,
    @SerializedName("cards_held_by_each_player")
    val cardsHeldByEachPlayer: List<Int>,
    @SerializedName("was_last_transaction_successful_declare")
    val wasLastTxnSuccessfulDeclare: Boolean
) : Parcelable