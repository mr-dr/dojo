package com.dojo.lit.lit.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.dojo.lit.R
import com.dojo.lit.Utils
import com.dojo.lit.fragment.BaseFragment
import com.dojo.lit.lit.BundleArgumentKeys
import com.dojo.lit.lit.PlayGameVM
import com.dojo.lit.lit.TransactionLogVM
import com.dojo.lit.lit.presenter.PlayGamePresenter
import com.dojo.lit.lit.util.SetNamesUtil
import com.dojo.lit.lit.view.IPlayGameView
import com.dojo.lit.util.CardImagesUtil
import com.dojo.lit.util.TextUtil
import java.lang.StringBuilder



class PlayGameFragment : BaseFragment(), IPlayGameView {

    private lateinit var mPresenter: PlayGamePresenter // change to interface

    private lateinit var mGameCodeTv: TextView
    private lateinit var mDroppedSetsTv: TextView
    private lateinit var mYourScoreTv: TextView
    private lateinit var mOpponentScoreTv: TextView
    private lateinit var mCardHeldLl: LinearLayout
    private lateinit var mLogsTv: TextView
    private lateinit var mTurnInfoTv: TextView
    private lateinit var mYourCardsLl: LinearLayout
    private lateinit var mActionsLl: LinearLayout
    private lateinit var mAskBtn: TextView
    private lateinit var mTransferBtn: TextView
    private lateinit var mDeclareBtn: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_play_lit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments == null) {
            Utils.makeToastLong(R.string.lit_game_not_enough_args)
            return
        }
        init()
        setupPresenter()
        mPresenter.start()
    }

    private fun init() {
        initViews()
        setupDefaultValues()
    }

    private fun initViews() {
        mGameCodeTv = findViewById(R.id.game_code_tv)
        mDroppedSetsTv = findViewById(R.id.dropped_set_info_tv)
        mYourScoreTv = findViewById(R.id.your_score_info_tv)
        mOpponentScoreTv = findViewById(R.id.opponent_score_info_tv)
        mCardHeldLl = findViewById(R.id.cards_held_table)
        mLogsTv = findViewById(R.id.logs_info_tv)
        mTurnInfoTv = findViewById(R.id.turn_info)
        mYourCardsLl = findViewById(R.id.your_cards_ll)
        mActionsLl = findViewById(R.id.action_btns_ll)
        mTransferBtn = findViewById(R.id.transfer_tv)
        mAskBtn = findViewById(R.id.ask_tv)
        mDeclareBtn = findViewById(R.id.declare_tv)
    }

    private fun setupDefaultValues() {
        val gameCode=
            arguments!!.getString(BundleArgumentKeys.GAME_CODE) ?: Utils.getString(R.string.none)
        mGameCodeTv.text = Utils.getString(R.string.game_code, gameCode)
    }

    private fun setupPresenter() {
        mPresenter = PlayGamePresenter(this, arguments!!)
    }

    override fun setData(vm: PlayGameVM) {
        mDroppedSetsTv.text = getDroppedSetsText(vm.droppedSets)
        mYourScoreTv.text = Utils.getString(R.string.your_score, vm.yourScore.toString())
        mOpponentScoreTv.text = Utils.getString(R.string.opponent_score, vm.opponentScore.toString())
        mTurnInfoTv.apply {
            if(vm.isYourTurn){
                mTurnInfoTv.text = Utils.getString(R.string.its_your_turn)
                mTurnInfoTv.setBackgroundColor(Utils.getColor(R.color.your_turn))
            } else {
                mTurnInfoTv.text = Utils.getString(R.string.waiting_for_turn, vm.nameOfPlayerWhoseTurn)
                mTurnInfoTv.setBackgroundColor(Utils.getColor(R.color.not_your_turn))
            }
        }
        mActionsLl.apply {
            if (vm.showDefaultActions) {
                visibility = VISIBLE
            } else {
                visibility = GONE
            }
        }
        mTransferBtn.apply {
            if (vm.showTransferAction) {
                visibility = VISIBLE
            } else {
                visibility = GONE
            }
        }
        if (vm.yourCardsChanged) {
            updateCardsInHand(vm.yourCards)
        }
        mLogsTv.text = getLogsText(vm.logs)
        if (vm.cardsHeldNoChanged) {
            updateCardsHeldByEachPlayer(vm.playerNames, vm.cardsHeldNo)
        }
    }

    private fun updateCardsHeldByEachPlayer(playerNames: List<String>, cardsHeldNo: List<Int>) {
        // it is assumed that player names and cards held no. are same size
        mCardHeldLl.removeAllViews()
        mCardHeldLl.apply {
            playerNames.indices.forEach { count ->
                val tv = TextView(this.context)
                tv.gravity = Gravity.CENTER_HORIZONTAL
                tv.text = playerNames[count] + TextUtil.NEWLINE + cardsHeldNo[count]
            }
        }
    }

    private fun getLogsText(logs: List<TransactionLogVM>): String {
        if(logs.isEmpty()) return TextUtil.EMPTY
        val strBuilder = StringBuilder()
        logs.indices.forEach { count ->
            val log = logs[count]
            if (count > 0) strBuilder.append(TextUtil.NEWLINE)
            val askedBy = if (PlayGamePresenter.YOU.equals(log.askedBy)) {Utils.getString(R.string.you)} else {log.askedBy}
            val askedFrom = if (PlayGamePresenter.YOU.equals(log.askedFrom)) {Utils.getString(R.string.you)} else {log.askedFrom}
            val logTemplateId = if (log.wasSuccessful) {R.string.log_template_took} else {R.string.log_template_ask}
            strBuilder.append(Utils.getString(logTemplateId, askedBy, askedFrom, log.askedFor))
        }
        return strBuilder.toString()
    }

    private fun updateCardsInHand(yourCards: List<String>) {
        mYourCardsLl.removeAllViews()
        yourCards.forEach { apiCardName ->
            val drawableId = CardImagesUtil.getMapping().get(apiCardName)
            val view: View
            if (drawableId != null) {
                view = ImageView(this.context)
                view.setImageResource(drawableId)
            } else {
                view = View(this.context)
                view.setBackgroundColor(Utils.getColor(R.color.black))
                // fixme log this, it should never happen
            }
            val lp = LinearLayout.LayoutParams(
                Utils.getDimen(R.dimen.playing_card_width).toInt(),
                Utils.getDimen(R.dimen.playing_card_height).toInt()
            )
            val lpMargin = Utils.getDimen(R.dimen.standard_margin_half).toInt()
            lp.setMargins(lpMargin, 0, lpMargin, 0)
            view.layoutParams = lp
            mYourCardsLl.addView(view)
        }
    }

    private fun getDroppedSetsText(droppedSets: List<String>): String {
        val str = StringBuilder()
        droppedSets.indices.forEach { setCount ->
            val setApiName = droppedSets[setCount]
            if (setCount > 0) {
                str.append(TextUtil.PIPE_SEPARATOR)
            }
            str.append(SetNamesUtil.getMapping().get(setApiName))
        }
        return str.toString()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.resume()
    }

    override fun onPause() {
        super.onPause()
        mPresenter.pause()
    }

    override fun onStop() {
        super.onStop()
        mPresenter.stop()
    }
}