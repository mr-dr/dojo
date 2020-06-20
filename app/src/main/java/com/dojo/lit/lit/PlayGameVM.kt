package com.dojo.lit.lit

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlayGameVM(
    val droppedSets: List<String>,
    val yourScore: Int,
    val opponentScore: Int,
    val cardsHeldNoChanged: Boolean,
    val reorderedCardsHeldNo: List<Int>,
    val reorderedPlayerNames: List<String>,
    val logs: List<TransactionLogVM>,
    val logsStr: String,
    val toastMessage: String?,
    val nameOfPlayerWhoseTurn: String,
    val isYourTurn: Boolean,
    val yourCards: List<String>,
    val yourCardsChanged: Boolean,
    val showDeclareAction: Boolean,
    val showTransferAction: Boolean
) : Parcelable

@Parcelize
data class TransactionLogVM(
    val askedBy: String,
    val askedFrom: String,
    val askedFor: String,
    val wasSuccessful: Boolean
) : Parcelable