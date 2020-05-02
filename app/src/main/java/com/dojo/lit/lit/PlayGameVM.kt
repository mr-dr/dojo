package com.dojo.lit.lit


data class PlayGameVM(
    val droppedSets: List<String>,
    val yourScore: Int,
    val opponentScore: Int,
    val cardsHeldNo: List<Int>,
    val log: String,
    val nameOfPlayerWhoseTurn: String,
    val isYourTurn: Boolean,
    val yourCards: List<String>,
    val showDefaultActions: Boolean,
    val showTransferAction: Boolean
) {
}