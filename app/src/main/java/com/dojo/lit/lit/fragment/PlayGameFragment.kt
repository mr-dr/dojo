package com.dojo.lit.lit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.dojo.lit.R
import com.dojo.lit.Utils
import com.dojo.lit.fragment.BaseFragment
import com.dojo.lit.lit.BundleArgumentKeys
import com.dojo.lit.lit.presenter.PlayGamePresenter
import com.dojo.lit.lit.view.IPlayGameView

class PlayGameFragment : BaseFragment(), IPlayGameView {
    private lateinit var mPresenter: PlayGamePresenter // change to interface

    private lateinit var mGameCodeTv: TextView
    private lateinit var mDroppedSetsTv: TextView
    private lateinit var mScoreTv: TextView
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

    override fun onResume() {
        super.onResume()
        mPresenter.resume()
    }

    override fun onPause() {
        super.onPause()
        mPresenter.pause()
    }

    private fun init() {
        initViews()
        setupDefaultValues()
    }

    private fun initViews() {
        mGameCodeTv = findViewById(R.id.game_code_tv)
        mDroppedSetsTv = findViewById(R.id.dropped_set_info_tv)
        mScoreTv = findViewById(R.id.score_info_tv)
        mCardHeldLl = findViewById(R.id.cards_held_table)
        mLogsTv = findViewById(R.id.logs_info_tv)
        mTurnInfoTv = findViewById(R.id.turn_info)
        mYourCardsLl = findViewById(R.id.your_cards_ll)
        mActionsLl = findViewById(R.id.action_btns_ll)
        mAskBtn = findViewById(R.id.ask_tv)
        mTransferBtn = findViewById(R.id.transfer_tv)
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

    override fun onStop() {
        super.onStop()
        mPresenter.stop()
    }
}