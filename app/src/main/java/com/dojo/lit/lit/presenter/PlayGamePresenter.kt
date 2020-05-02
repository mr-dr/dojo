package com.dojo.lit.lit.presenter

import android.os.Bundle
import android.util.Log
import com.android.volley.VolleyError
import com.dojo.lit.base.BasePresenter
import com.dojo.lit.lit.BundleArgumentKeys
import com.dojo.lit.lit.PlayGameInteractor
import com.dojo.lit.lit.PlayGameVM
import com.dojo.lit.lit.TransactionLogVM
import com.dojo.lit.lit.model.TransactionData
import com.dojo.lit.lit.model.TransactionResponse
import com.dojo.lit.lit.view.IPlayGameView
import com.dojo.lit.network.ApiListeners
import com.dojo.lit.scheduling.GameStateUpdateScheduler
import com.dojo.lit.util.TextUtil

class PlayGamePresenter(val view: IPlayGameView, val arguments: Bundle) : BasePresenter() {
    companion object {
        val YOU = "you"
    }
    private val checkStatusAfterMillis = 500L

    private val yourPlayerNo: Int
    private val playerNames: MutableList<String>
    private val cardsInHand: MutableList<String>
    private var cardsInHandChanged: Boolean
    private var cardsHeldByEachPlayerChanged: Boolean
    private val cardsHeldByEachPlayer: MutableList<Int>
    private val logsLength: Int
    private val logs: MutableList<TransactionData>
    private var turnOfPlayer: Int
    private val droppedSets: MutableList<String>
    // received for every transaction, only true when last transaction was a successful declare by you
    private var droppedSuccessfullyInLastTurn: Boolean
    private var yourScore:Int
    private val opponentScore: Int
    get() = (droppedSets.size - yourScore)

    private val mInteractor: PlayGameInteractor
    private var isPaused: Boolean
    private val LOG_TAG = "play_lit_presenter"

    init {
        turnOfPlayer = 0
        yourScore = 0
        droppedSets = ArrayList()
        cardsHeldByEachPlayerChanged = false
        yourPlayerNo = arguments.getInt(BundleArgumentKeys.PLAYER_NO) // fixed value
        playerNames = arguments.getStringArrayList(BundleArgumentKeys.PLAYER_NAMES)!! // fixme decide
        cardsInHand = arguments.getStringArrayList(BundleArgumentKeys.CARDS_IN_HAND)!!
        cardsInHandChanged = false
        cardsHeldByEachPlayer = arguments.getIntegerArrayList(BundleArgumentKeys.CARDS_NO_EACH_PLAYER)!!
        logsLength = arguments.getInt(BundleArgumentKeys.LOGS_LENGTH) // fixed value
        droppedSuccessfullyInLastTurn = false
        logs = ArrayList()

        mInteractor = PlayGameInteractor()
        isPaused = false
    }

    private fun updatePresenterData(newData: TransactionResponse) {
        turnOfPlayer = newData.turnOfPlayer
        yourScore = newData.setsDeclaredByYourTeam
        cardsInHandChanged = !cardsInHand.equals(newData.yourCards) // expecting sorted cards
        cardsInHand.clear()
        cardsInHand.addAll(newData.yourCards)
        droppedSets.clear()
        droppedSets.addAll(newData.droppedSets)
        cardsHeldByEachPlayerChanged = !cardsHeldByEachPlayer.equals(newData.cardsHeldByEachPlayer)
                || !playerNames.equals(newData.playerNames)
        playerNames.clear()
        playerNames.addAll(newData.playerNames)
        cardsHeldByEachPlayer.clear()
        cardsHeldByEachPlayer.addAll(newData.cardsHeldByEachPlayer)
        droppedSuccessfullyInLastTurn = newData.wasLastTxnSuccessfulDrop
        if (newData.lastTransaction != null) {
            addNewLog(newData.lastTransaction)
        }
    }

    private fun updateState(response: TransactionResponse) {
        updatePresenterData(response)
        setData()
    }

    override fun start(){
        setData() // initialize data
        GameStateUpdateScheduler.schedule(Runnable { // schedule update after regular interval
            fetchChanges()
        }, checkStatusAfterMillis)
    }

    private fun fetchChanges() {
        if (yourPlayerNo == turnOfPlayer || isPaused) {
            Log.d(LOG_TAG, "fetchChanges() called but skipped")
            return
        }
        mInteractor.fetchChanges(object : ApiListeners<TransactionResponse>() {
            override fun onErrorResponse(error: VolleyError) {
                Log.d(LOG_TAG, "fetch changes failed")
            }

            override fun onResponse(response: TransactionResponse?) {
                if (response == null) return
                updateState(response)
            }
        })

    }

    private fun setData() {
        view.setData(getVM())
    }

    private fun getVM(): PlayGameVM {
        val logsVM = ArrayList<TransactionLogVM>()
        logs.forEach { log ->
            logsVM.add(TransactionLogVM(
                if (log.askedBy != yourPlayerNo) {playerNames[log.askedBy]} else {YOU},
                if (log.askedBy != yourPlayerNo) {playerNames[log.askedFrom]} else {YOU},
                log.askedFor,
                log.wasSuccessful
            ))
        }
        val nameOfCurrentTurnPlayer = playerNames[turnOfPlayer]
        val isYourTurn = turnOfPlayer == yourPlayerNo

        return PlayGameVM(
            droppedSets,
            yourScore,
            opponentScore,
            cardsHeldByEachPlayerChanged,
            cardsHeldByEachPlayer,
            playerNames,
            logsVM,
            nameOfCurrentTurnPlayer,
            isYourTurn,
            cardsInHand,
            cardsInHandChanged,
            isYourTurn,
            droppedSuccessfullyInLastTurn
            )
    }

    private fun addNewLog(log: TransactionData) {
        if (!logs.isEmpty() && logs[logs.size - 1].equals(log)) {
            return
        }
        logs.add(log)
        if (logs.size > logsLength) {
            logs.subList(logs.size - logsLength, logs.size)
        }
    }

    override fun stop() {
        super.stop()
        GameStateUpdateScheduler.stopScheduledTask()
    }

    fun resume() {
        isPaused = false
    }

    fun pause() {
        isPaused = true
    }

}