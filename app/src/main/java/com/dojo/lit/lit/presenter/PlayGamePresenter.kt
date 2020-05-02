package com.dojo.lit.lit.presenter

import android.os.Bundle
import android.util.Log
import com.android.volley.VolleyError
import com.dojo.lit.base.BasePresenter
import com.dojo.lit.lit.BundleArgumentKeys
import com.dojo.lit.lit.PlayGameInteractor
import com.dojo.lit.lit.PlayGameVM
import com.dojo.lit.lit.model.TransactionResponse
import com.dojo.lit.lit.view.IPlayGameView
import com.dojo.lit.network.ApiListeners
import com.dojo.lit.scheduling.GameStateUpdateScheduler
import com.dojo.lit.util.TextUtil

class PlayGamePresenter(val view: IPlayGameView, val arguments: Bundle) : BasePresenter() {
    private val checkStatusAfterMillis = 500L

    private val yourPlayerNo: Int
    private val playerNames: MutableList<String>
    private var cardsInHand: MutableList<String>
    private val cardsHeldByEachPlayer: MutableList<Int>
    private val logsLength: Int
    private val logs: MutableList<String>
    private var turnOfPlayer: Int
    private var droppedSets: MutableList<String>
    // received for every transaction, only true when last transaction was a successful declare by you
    private var declaredSuccessfullyInLastTurn: Boolean
    private var yourScore:Int
    private val opponentScore: Int
    get() = (droppedSets.size - yourScore)

    private val mInteractor: PlayGameInteractor
    private var isPaused: Boolean
    private val LOG_TAG = "scheduler"

    init {
        turnOfPlayer = 0
        droppedSets = ArrayList()
        yourPlayerNo = arguments.getInt(BundleArgumentKeys.PLAYER_NO)
        playerNames = arguments.getStringArrayList(BundleArgumentKeys.PLAYER_NAMES)!!
        cardsInHand = arguments.getStringArrayList(BundleArgumentKeys.CARDS_IN_HAND)!!
        cardsHeldByEachPlayer = arguments.getIntegerArrayList(BundleArgumentKeys.CARDS_NO_EACH_PLAYER)!!
        logsLength = arguments.getInt(BundleArgumentKeys.LOGS_LENGTH)
        logs = ArrayList()
        mInteractor = PlayGameInteractor()
        declaredSuccessfullyInLastTurn = false
        yourScore = 0
        isPaused = false
    }

    private fun updatePresenterData(newData: TransactionResponse) {
        turnOfPlayer = newData.turnOfPlayer
        yourScore = newData.setsDeclaredByYourTeam
        cardsInHand = newData.yourCards
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
        val logsStr = StringBuilder()
        logs.indices.forEach { logCount ->
            val log = logs[logCount]
            logsStr.append(log)
            if (logCount < logs.size - 1) {
                logsStr.append(TextUtil.NEWLINE)
            }
        }
        val nameOfCurrentTurnPlayer = playerNames[turnOfPlayer]
        val isYourTurn = turnOfPlayer == yourPlayerNo
        return PlayGameVM(
            droppedSets,
            yourScore,
            opponentScore,
            cardsHeldByEachPlayer,
            logsStr.toString(),
            nameOfCurrentTurnPlayer,
            isYourTurn,
            cardsInHand,
            isYourTurn,
            declaredSuccessfullyInLastTurn
            )
    }

    private fun addNewLog(log: String) {
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