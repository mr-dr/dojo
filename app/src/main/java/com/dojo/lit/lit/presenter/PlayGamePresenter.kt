package com.dojo.lit.lit.presenter

import android.os.Bundle
import android.util.Log
import com.android.volley.VolleyError
import com.dojo.lit.base.BasePresenter
import com.dojo.lit.lit.BundleArgumentKeys
import com.dojo.lit.lit.GameInteractor
import com.dojo.lit.lit.PlayGameVM
import com.dojo.lit.lit.TransactionLogVM
import com.dojo.lit.lit.model.DropSetData
import com.dojo.lit.lit.model.TransactionData
import com.dojo.lit.lit.model.TransactionResponse
import com.dojo.lit.lit.util.CardToSetNameUtil
import com.dojo.lit.lit.view.IPlayGameView
import com.dojo.lit.network.ApiListeners
import com.dojo.lit.util.FirebaseRealtimeDbListener
import com.dojo.lit.util.FirebaseUtils
import com.dojo.lit.util.TextUtil
import com.google.android.gms.common.util.CollectionUtils
import com.google.firebase.database.DatabaseException

class PlayGamePresenter(var view: IPlayGameView?, val arguments: Bundle?) : BasePresenter() {
    // fixme arguments should be non-null
    companion object {
        val YOU = "you"
    }
    private val checkStatusAfterMillis = 500L

    val gameCode: String
    private val yourPlayerNo: Int
    private val playerNames: MutableList<String>
    private val cardsInHand: MutableList<String>
    private var cardsInHandChanged: Boolean
    private var cardsHeldByEachPlayerChanged: Boolean
    private val cardsHeldByEachPlayer: MutableList<Int>
    private val logsLength: Int
    private var logsStr: String
    private var showToastMessage = true
    private var toastMessage: String?
    @Deprecated("use logsStr instead") private val logs: MutableList<TransactionData>
    private var turnOfPlayer: Int
    val droppedSets: MutableList<String>
    // received for every transaction, only true when last transaction was a successful declare by you
    private var droppedSuccessfullyInLastTurn: Boolean?
    private var yourScore:Int
    private var opponentScore: Int

    private val mInteractor: GameInteractor
    private var isPaused: Boolean
    private val LOG_TAG = "play_lit_presenter"

    private var firebaseListener: FirebaseRealtimeDbListener? = null

    init {
        turnOfPlayer = 1 // can be 1-6
        yourScore = 0
        opponentScore = 0
        droppedSets = ArrayList()
        cardsHeldByEachPlayerChanged = false
        gameCode = arguments?.getString(BundleArgumentKeys.GAME_CODE) ?: TextUtil.EMPTY
        yourPlayerNo = arguments?.getInt(BundleArgumentKeys.PLAYER_NO) ?: 0 // fixed value
        playerNames = arguments?.getStringArrayList(BundleArgumentKeys.PLAYER_NAMES) ?: ArrayList()// fixme decide
        cardsInHand = arguments?.getStringArrayList(BundleArgumentKeys.CARDS_IN_HAND) ?: ArrayList()
        cardsInHandChanged = false
        cardsHeldByEachPlayer = arguments?.getIntegerArrayList(BundleArgumentKeys.CARDS_NO_EACH_PLAYER) ?: ArrayList()
        logsLength = arguments?.getInt(BundleArgumentKeys.LOGS_LENGTH) ?: 0 // fixed value
        droppedSuccessfullyInLastTurn = null
        logsStr = "-"
        toastMessage = null
        logs = ArrayList()

        mInteractor = GameInteractor()
        isPaused = false
    }

    private fun <T> reorderOnbasisOfPlayerNo(listToRotate: List<T>?): List<T>? {
        if(CollectionUtils.isEmpty(listToRotate)) {
            return null
        }
        var list = ArrayList<T>()
        for (x in 0 until (yourPlayerNo-1)) {
            var element = listToRotate!!.get(x)
            list.add(element)
        }

        for (y in 5 downTo yourPlayerNo-1) {
            var element = listToRotate!!.get(y)
            list.add(0,element)
        }
        return list
    }


    private fun updatePresenterData(newData: TransactionResponse) {
        turnOfPlayer = newData.turnOfPlayer
        yourScore = if (youAreOnOddTeam()) newData.scoreOddTeam else newData.scoreEvenTeam
        opponentScore = if (youAreOnOddTeam()) newData.scoreEvenTeam else newData.scoreOddTeam
        val newCardsInHand = getYourCards(newData)
        cardsInHandChanged = !cardsInHand.equals(newCardsInHand) // expecting sorted cards
        cardsInHand.clear()
        if (newCardsInHand != null) cardsInHand.addAll(newCardsInHand) // fixme
        droppedSets.clear()
        if (newData.droppedSets != null) droppedSets.addAll(newData.droppedSets)  // fixme
        logsStr = TextUtil.join(newData.logs, TextUtil.NEWLINE);
        val lastLog =
            if (newData.logs != null && newData.logs.size > 0) newData.logs[newData.logs.size - 1] else null
        if (!TextUtil.areSame(toastMessage, lastLog) && !TextUtil.isEmpty(lastLog)) {
            toastMessage = lastLog
            showToastMessage = true
        } else {
            showToastMessage = false
        }
        cardsHeldByEachPlayerChanged = !cardsHeldByEachPlayer.equals(newData.getCardsHeldByEachPlayer())
                || !playerNames.equals(newData.getPlayerNames())
        playerNames.clear()
        playerNames.addAll(newData.getPlayerNames())
        cardsHeldByEachPlayer.clear()
        cardsHeldByEachPlayer.addAll(newData.getCardsHeldByEachPlayer())
        droppedSuccessfullyInLastTurn = newData.wasLastTxnSuccessfulDrop?.toBoolean()

    }

    private fun getYourCards(newData: TransactionResponse): List<String> {
        if (yourPlayerNo == 1) {
            return newData.player1.cards ?: ArrayList()
        } else if (yourPlayerNo == 2) {
            return newData.player2.cards ?: ArrayList()
        } else if (yourPlayerNo == 3) {
            return newData.player3.cards ?: ArrayList()
        } else if (yourPlayerNo == 4) {
            return newData.player4.cards ?: ArrayList()
        } else if (yourPlayerNo == 5) {
            return newData.player5?.cards ?: ArrayList()
        } else {
            return newData.player6?.cards ?: ArrayList()
        }
    }

    private fun updateState(response: TransactionResponse) {
        updatePresenterData(response)
        setData()
    }

    override fun start(){
        setData() // initialize data
        FirebaseUtils.connectToDb(gameCode)
        firebaseListener = object : FirebaseRealtimeDbListener() {
            override fun onDataChange(value: TransactionResponse?) {
                if (value == null) return
                updateState(value) // fixme
            }

            override fun onCancelled(exception: DatabaseException) {
                // TODO log error for firebase DB
            }
        }
        if(firebaseListener != null) {
            FirebaseUtils.subscribeToFirebaseRealtimeDb(firebaseListener!!)
        }
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
        if (view?.isDead() ?: true) return
        val vm = getVM()
        view?.setData(vm)
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
            if (playerNames.size > turnOfPlayer - 1 && playerNames[turnOfPlayer - 1] != null) playerNames[turnOfPlayer - 1] else "-"
        val isYourTurn = turnOfPlayer == yourPlayerNo

        val toastMsg = if (showToastMessage) toastMessage else null

        val reorderedCardsHeldNo = if (cardsHeldByEachPlayer.isEmpty()) cardsHeldByEachPlayer else reorderOnbasisOfPlayerNo(cardsHeldByEachPlayer)
        val reorderedPlayerNames = if (playerNames.isEmpty()) playerNames else reorderOnbasisOfPlayerNo(playerNames)

        val showTransferAction = droppedSuccessfullyInLastTurn != null && isYourTurn

        return PlayGameVM(
            droppedSets,
            yourScore,
            opponentScore,
            cardsHeldByEachPlayerChanged,
            reorderedCardsHeldNo!!,
            reorderedPlayerNames!!,
            logsVM,
            logsStr,
            toastMsg,
            nameOfCurrentTurnPlayer,
            isYourTurn,
            cardsInHand,
            cardsInHandChanged,
            droppedSuccessfullyInLastTurn?:true&&isYourTurn,
            showTransferAction
            )
    }


    override fun stop() { // fixme rishabh
        super.stop()
        Log.d(LOG_TAG, "onStop() called")
//        FirebaseUtils.disconnectFromDb()
        FirebaseUtils.unsubscribeToFirebaseRealtimeDb(firebaseListener)
        view = null
//        mInteractor.leaveRoom(gameCode, yourPlayerNo, object : ApiListeners<String>() {
//            override fun onErrorResponse(error: VolleyError) {
//                Log.d(LOG_TAG, "leave room failed")
//            }
//
//            override fun onResponse(response: String?) {
//
//            }
//        })
////        GameStateUpdateScheduler.stopScheduledTask()
    }

    override fun destroy() {
        Log.d(LOG_TAG, "onDestroy() called")
    }

    fun resume() {
        isPaused = false
    }

    fun pause() {
        isPaused = true
    }

    fun getOppositeTeamPlayerNames(): List<String> {
        val arr = ArrayList<String>(3)
        if (playerNames.size < 6) return arr
        if (youAreOnOddTeam()) { // is player 1, 3 or 5
            arr.add(playerNames[1])
            arr.add(playerNames[3])
            arr.add(playerNames[5])
        } else { // player 2, 4 or 6
            arr.add(playerNames[0])
            arr.add(playerNames[2])
            arr.add(playerNames[4])
        }
        return arr
    }

    fun getSameTeamPlayerNames(giveOwnName: Boolean): List<String> {
        val arr = ArrayList<String>(3)
        if (playerNames.size < 6) return arr
        if (youAreOnOddTeam()) { // is player 1, 3 or 5
            if (yourPlayerNo - 1 != 0) arr.add(playerNames[0])
            if (yourPlayerNo - 1 != 2) arr.add(playerNames[2])
            if (yourPlayerNo - 1 != 4) arr.add(playerNames[4])
        } else { // player 2, 4 or 6
            if (yourPlayerNo - 1 != 1) arr.add(playerNames[1])
            if (yourPlayerNo - 1 != 3) arr.add(playerNames[3])
            if (yourPlayerNo - 1 != 5) arr.add(playerNames[5])
        }
        if(giveOwnName) {
            arr.add(0,playerNames[yourPlayerNo-1])
        }
        return arr
    }

    private fun yourTeamHasCards(): Boolean {
        if (youAreOnOddTeam()) { // is player 1, 3 or 5
            if(cardsHeldByEachPlayer[0] > 0 || cardsHeldByEachPlayer[2] > 0 || cardsHeldByEachPlayer[4] > 0) {
                return true
            }
        } else { // player 2, 4 or 6
            if(cardsHeldByEachPlayer[1] > 0 || cardsHeldByEachPlayer[3] > 0 || cardsHeldByEachPlayer[5] > 0) {
                return true
            }
        }
        return false
    }

    private fun youAreOnOddTeam(): Boolean {
        return (yourPlayerNo % 2 == 1)
    }

    fun getTransferablePlayerNames(): List<String> {
        val yourTeamHasCards = yourTeamHasCards()
        if (yourTeamHasCards && droppedSuccessfullyInLastTurn!!) {
            return getSameTeamPlayerNames(false)
        } else {
            return getOppositeTeamPlayerNames()
        }
    }

    // TODO don't return sets all cards of which are with you
    fun getAskableSetNames(): List<String> {
        val arr = HashSet<String>()
        cardsInHand.forEach {
            arr.add(CardToSetNameUtil.getMapping().get(it)!!)
        }
        return arr.toList()
    }

    fun getCardsForSet(selectedSet: String?): List<String> {
        val arr = ArrayList<String>()
        val mapping = CardToSetNameUtil.getMapping()
        mapping.keys.forEach { card ->
            val setName = mapping.get(card)
            if (setName == selectedSet) {
                arr.add(card)
            }
        }
        return arr
    }

    fun getAskableCards(selectedSet: String?): List<String> {
        val arr = ArrayList<String>()
        val mapping = CardToSetNameUtil.getMapping()
        mapping.keys.forEach { card ->
            val setName = mapping.get(card)
            if (setName == selectedSet && !cardsInHand.contains(card)) {
                arr.add(card)
            }
        }
        return arr
    }

    fun askCard(playerName: String, card: String) {
        val data = TransactionData(yourPlayerNo, getPlayerNoFromName(playerName), card, false)
        mInteractor.askCard(gameCode, data, object: ApiListeners<String>(){
            override fun onResponse(response: String?) {

            }

            override fun onErrorResponse(error: VolleyError?) {
                // TODO log this
            }

        })
    }

    fun transferTurn(playerName: String) {
        mInteractor.transferTurn(yourPlayerNo, getPlayerNoFromName(playerName), gameCode, object: ApiListeners<String>(){
            override fun onResponse(response: String?) {

            }

            override fun onErrorResponse(error: VolleyError?) {
                // TODO log this
            }

        })
    }

    private fun getPlayerNoFromName(playerName: String): Int { // returns 1-6
        playerNames.indices.forEach {
            if(playerName == playerNames[it]) {
                return (it + 1)
            }
        }
        return -1
    }

    fun dropSet(
        player1Cards: List<String>,
        player2Cards: List<String>,
        player3Cards: List<String>
    ) {
        val data = if (youAreOnOddTeam()) {
            DropSetData(1, 3, 5, player1Cards, player2Cards, player3Cards)
        } else {
            DropSetData(2, 4, 6, player1Cards, player2Cards, player3Cards)
        }
        mInteractor.dropSet(gameCode, data, object: ApiListeners<String>(){
            override fun onResponse(response: String?) {

            }

            override fun onErrorResponse(error: VolleyError?) {
                // TODO log
            }

        })
    }

    fun rematch(gameCode: String) {
        // TODO implement
    }

}