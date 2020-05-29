package com.dojo.lit.lit.presenter

import android.os.Bundle
import android.util.Log
import com.android.volley.VolleyError
import com.dojo.lit.base.BasePresenter
import com.dojo.lit.lit.BundleArgumentKeys
import com.dojo.lit.lit.GameInteractor
import com.dojo.lit.lit.PlayGameVM
import com.dojo.lit.lit.TransactionLogVM
import com.dojo.lit.lit.model.TransactionData
import com.dojo.lit.lit.model.TransactionResponse
import com.dojo.lit.lit.view.IPlayGameView
import com.dojo.lit.network.ApiListeners
import com.dojo.lit.scheduling.GameStateUpdateScheduler
import com.dojo.lit.util.FirebaseRealtimeDbListener
import com.dojo.lit.util.FirebaseUtils
import com.google.firebase.database.DatabaseException

class PlayGamePresenter(val view: IPlayGameView, val arguments: Bundle?) : BasePresenter() {
    // fixme arguments should be non-null
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

    private val mInteractor: GameInteractor
    private var isPaused: Boolean
    private val LOG_TAG = "play_lit_presenter"

    init {
        turnOfPlayer = 0
        yourScore = 0
        droppedSets = ArrayList()
        cardsHeldByEachPlayerChanged = false
        yourPlayerNo = arguments?.getInt(BundleArgumentKeys.PLAYER_NO) ?: 0 // fixed value
        playerNames = arguments?.getStringArrayList(BundleArgumentKeys.PLAYER_NAMES) ?: ArrayList() // fixme decide
        cardsInHand = arguments?.getStringArrayList(BundleArgumentKeys.CARDS_IN_HAND) ?: ArrayList()
        cardsInHandChanged = false
        cardsHeldByEachPlayer = arguments?.getIntegerArrayList(BundleArgumentKeys.CARDS_NO_EACH_PLAYER) ?: ArrayList()
        logsLength = arguments?.getInt(BundleArgumentKeys.LOGS_LENGTH) ?: 0 // fixed value
        droppedSuccessfullyInLastTurn = false
        logs = ArrayList()

        mInteractor = GameInteractor()
        isPaused = false
    }

    private fun updatePresenterData(newData: TransactionResponse) {
        val scoreArray = newData.scoreboardText.split(":")
        turnOfPlayer = newData.turnOfPlayer
        yourScore = (scoreArray.get(0)).toInt()
        val newCardsInHand = getYourCards(newData)
        cardsInHandChanged = !cardsInHand.equals(newCardsInHand) // expecting sorted cards
        cardsInHand.clear()
        cardsInHand.addAll(newCardsInHand)
        droppedSets.clear()
//        droppedSets.addAll(newData.droppedSets) // fixme uncomment
//        cardsHeldByEachPlayerChanged = !cardsHeldByEachPlayer.equals(newData.cardsHeldByEachPlayer)
//                || !playerNames.equals(newData.playerNames)
        playerNames.clear()
        playerNames.addAll(getPlayerNames(newData))
//        cardsHeldByEachPlayer.clear()
//        cardsHeldByEachPlayer.addAll(newData.cardsHeldByEachPlayer)
        droppedSuccessfullyInLastTurn = newData.wasLastTxnSuccessfulDrop.toBoolean()
//        if (newData.lastTransaction != null) {
//            addNewLog(newData.lastTransaction)
//        }
    }

    private fun getYourCards(newData: TransactionResponse): List<String> {
        if (yourPlayerNo == 1) {
            return newData.player1.cards
        } else if (yourPlayerNo == 2) {
            return newData.player2.cards
        } else if (yourPlayerNo == 3) {
            return newData.player3.cards
        } else if (yourPlayerNo == 4) {
            return newData.player4.cards
        } else if (yourPlayerNo == 5) {
            return newData.player5.cards
        } else {
            return newData.player6.cards
        }
    }

    private fun getPlayerNames(newData: TransactionResponse): List<String> {
        val names = ArrayList<String>()
        names.add(newData.player1.name)
        names.add(newData.player2.name)
        names.add(newData.player3.name)
        names.add(newData.player4.name)
        names.add(newData.player5.name)
        names.add(newData.player6.name)
        return names
    }

    private fun updateState(response: TransactionResponse) {
        updatePresenterData(response)
        setData()
    }

    override fun start(){
        setData() // initialize data
        FirebaseUtils.subscribeToFirebaseRealtimeDb(object: FirebaseRealtimeDbListener() {
            override fun onDataChange(value: TransactionResponse?) {
                if (value == null) return
                updateState(value) // fixme
            }

            override fun onCancelled(exception: DatabaseException) {
                // TODO log error for firebase DB
            }
        })
//        { // TODO will be useful in future on shifting from realtime DB
//            GameStateUpdateScheduler.schedule(Runnable { // schedule update after regular interval
//                fetchChanges()
//            }, checkStatusAfterMillis)
//        }
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
        val nameOfCurrentTurnPlayer =
            if (playerNames.size > turnOfPlayer) playerNames[turnOfPlayer] else "-"
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