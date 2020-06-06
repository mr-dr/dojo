package com.dojo.lit.lit.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionResponse(
    @SerializedName("turn")
    val turnOfPlayer: Int,
    @SerializedName("dropped_sets")
    val droppedSets: List<String>,
    @SerializedName("last_transaction_drop")
    val wasLastTxnSuccessfulDrop: String, // fixme - backend
    @SerializedName("score_even")
    val scoreEvenTeam: Int,
    @SerializedName("score_odd")
    val scoreOddTeam: Int,
    val logs: List<String>?,
    @SerializedName("1")
    val player1: PlayerInfo,
    @SerializedName("2")
    val player2: PlayerInfo,
    @SerializedName("3")
    val player3: PlayerInfo,
    @SerializedName("4")
    val player4: PlayerInfo,
    @SerializedName("5")
    val player5: PlayerInfo?,
    @SerializedName("6")
    val player6: PlayerInfo?
//    @SerializedName("last_transaction")
//    val lastTransaction: TransactionData? // null in case of set drop etc
) : Parcelable {

    fun getCardsHeldByEachPlayer() : List<Int> {
        val cardsHeld = ArrayList<Int>()
        cardsHeld.add(player1.cards.size)
        cardsHeld.add(player2.cards.size)
        cardsHeld.add(player3.cards.size)
        cardsHeld.add(player4.cards.size)
        if (player5 != null) {
            cardsHeld.add(player5.cards.size)
        }
        if (player6 != null) {
            cardsHeld.add(player6.cards.size)
        }
        return cardsHeld
    }

    fun getPlayerNames() : List<String> {
        val playerNames = ArrayList<String>()
        playerNames.add(player1.name)
        playerNames.add(player2.name)
        playerNames.add(player3.name)
        playerNames.add(player4.name)
        if (player5 != null) {
            playerNames.add(player5.name)
        }
        if (player6 != null) {
            playerNames.add(player6.name)
        }
        return playerNames
    }
}

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