package com.dojo.lit.lit.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
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
import android.os.Handler
import android.widget.*
import androidx.appcompat.app.AlertDialog
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.dojo.lit.util.CardNamesUtil
import com.dojo.lit.view.DraggableTextView
import com.dojo.lit.view.DroppableLinearLayout
import androidx.core.view.children
import com.dojo.lit.view.Draggable

class PlayGameFragment : BaseFragment(), IPlayGameView, View.OnClickListener {
    override fun onClick(v: View?) {
        if (v == null) return
        val id = v.id
        if (id == R.id.ask_tv) {
            showAskDialog()
        } else if (id == R.id.transfer_tv) {
            showTransferDialog()
        } else if (id == R.id.declare_tv) {
            showDeclareSetDialog()
        } else if (id == R.id.game_code_tv || id == R.id.game_code_share) {
            shareGameCode(mPresenter.gameCode)
        } else if (id == R.id.player_1_opp_tv || id == R.id.player_2_opp_tv || id == R.id.player_3_opp_tv) {
            showAskDialog((v as TextView).text.toString())
        }
    }

    private fun shareGameCode(gameCode: String) {
        // fixme - move to strings.xml
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        share.putExtra(Intent.EXTRA_TEXT, "Join our game room - " + gameCode)
        startActivity(Intent.createChooser(share, "Share game code"))
    }

    private lateinit var mPresenter: PlayGamePresenter // change to interface

    private lateinit var mGameCodeTv: TextView
    private lateinit var mGameCodeShareTv: View
    private lateinit var mDroppedSetsTv: TextView
    private lateinit var mYourScoreTv: TextView
    private lateinit var mOpponentScoreTv: TextView
    private lateinit var mCardHeldLl: LinearLayout
    private lateinit var mLogsTv: TextView
    private lateinit var mLogsSv: ScrollView
    private lateinit var mTurnInfoTv: TextView
    private lateinit var mYourCardsLl: LinearLayout
    private lateinit var mOppPlayerNameTv1: TextView
    private lateinit var mOppPlayerNameTv2: TextView
    private lateinit var mOppPlayerNameTv3: TextView
    private lateinit var mSamePlayerNameTv1: TextView
    private lateinit var mSamePlayerNameTv2: TextView
    private lateinit var mActionsLl: LinearLayout
    private lateinit var mAskBtn: TextView
    private lateinit var mTransferBtn: TextView
    private lateinit var mDeclareBtn: TextView
    private lateinit var mYourAlias: TextView
    private val mHandler = Handler()
    private val delayedTimeMillis: Long = 5000

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupPresenter()
        return inflater.inflate(R.layout.fragment_play_lit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments == null) {
            Utils.makeToastLong(R.string.lit_game_not_enough_args)
            return
        }
        init()
        mPresenter.start()
    }

    private fun init() {
        initViews()
        setupDefaultValues()
    }

    private fun initViews() {
        mGameCodeTv = findViewById(R.id.game_code_tv)
        mGameCodeShareTv = findViewById(R.id.game_code_share)
        mDroppedSetsTv = findViewById(R.id.dropped_set_info_tv)
        mYourScoreTv = findViewById(R.id.your_score_info_tv)
        mYourAlias = findViewById(R.id.your_alias_tv)
        mOpponentScoreTv = findViewById(R.id.opponent_score_info_tv)
        mCardHeldLl = findViewById(R.id.cards_held_table)
        mLogsTv = findViewById(R.id.logs_info_tv)
        mLogsSv = findViewById(R.id.logs_info_sv)
        mTurnInfoTv = findViewById(R.id.turn_info)
        mYourCardsLl = findViewById(R.id.your_cards_ll)
        mOppPlayerNameTv1 = findViewById(R.id.player_1_opp_tv)
        mOppPlayerNameTv2 = findViewById(R.id.player_2_opp_tv)
        mOppPlayerNameTv3 = findViewById(R.id.player_3_opp_tv)
        mSamePlayerNameTv1 = findViewById(R.id.player_1_same_tv)
        mSamePlayerNameTv2 = findViewById(R.id.player_2_same_tv)
        mActionsLl = findViewById(R.id.action_btns_ll)
        mTransferBtn = findViewById(R.id.transfer_tv)
        mAskBtn = findViewById(R.id.ask_tv)
        mDeclareBtn = findViewById(R.id.declare_tv)


        mTransferBtn.setOnClickListener(this)
        mAskBtn.setOnClickListener(this)
        mDeclareBtn.setOnClickListener(this)
        mGameCodeTv.setOnClickListener(this)
        mGameCodeShareTv.setOnClickListener(this)
        mLogsSv.post { mLogsSv.fullScroll(View.FOCUS_DOWN) } // scroll to bottom
    }

    private fun setupDefaultValues() {
        val gameCode =
            arguments!!.getString(BundleArgumentKeys.GAME_CODE)
                ?: getResources().getString(R.string.none)
        mGameCodeTv.text = getResources().getString(R.string.game_code, gameCode)
        val alias =
            arguments!!.getString(BundleArgumentKeys.ALIAS)
                ?: getResources().getString(R.string.none)
        mYourAlias.text = getResources().getString(R.string.your_alias, alias)
    }

    private fun setupPresenter() {
        mPresenter = PlayGamePresenter(this, arguments)
    }

    override fun setData(vm: PlayGameVM) {
        mDroppedSetsTv.text = getDroppedSetsText(vm.droppedSets)
        mYourScoreTv.text = getResources().getString(R.string.your_score, vm.yourScore.toString())
        mOpponentScoreTv.text =
            getResources().getString(R.string.opponent_score, vm.opponentScore.toString())
        mTurnInfoTv.apply {
            if (vm.isYourTurn) {
                mTurnInfoTv.text = getResources().getString(R.string.its_your_turn)
                mTurnInfoTv.setBackgroundColor(Utils.getColor(R.color.your_turn))
            } else {
                mTurnInfoTv.text =
                    getResources().getString(R.string.waiting_for_turn, vm.nameOfPlayerWhoseTurn)
                mTurnInfoTv.setBackgroundColor(Utils.getColor(R.color.not_your_turn))
            }
        }
        mActionsLl.apply {
            if (vm.showDefaultActions) {
                visibility = VISIBLE
                mOppPlayerNameTv1.setOnClickListener(this@PlayGameFragment)
                mOppPlayerNameTv2.setOnClickListener(this@PlayGameFragment)
                mOppPlayerNameTv3.setOnClickListener(this@PlayGameFragment)
            } else {
                visibility = GONE
                mOppPlayerNameTv1.setOnClickListener(null)
                mOppPlayerNameTv2.setOnClickListener(null)
                mOppPlayerNameTv3.setOnClickListener(null)
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
        if (!TextUtil.isEmpty(vm.toastMessage)) showDojoToast(vm.toastMessage!!, Toast.LENGTH_LONG)
        mLogsTv.text = vm.logsStr
        if (vm.cardsHeldNoChanged) {
            updatePlayerNamesNCards(vm.reorderedPlayerNames, vm.reorderedCardsHeldNo)
        }
    }

    private fun updatePlayerNamesNCards(
        playerNames: List<String>,
        cardsHeldNo: List<Int>
    ) {
        if (playerNames.size > 4) { // fixme condition shouldn't be needed
            mOppPlayerNameTv1.text =
                getString(R.string.player_name_cards, playerNames[1], cardsHeldNo[1].toString())
            mOppPlayerNameTv2.text =
                getString(R.string.player_name_cards, playerNames[3], cardsHeldNo[3].toString())
            mOppPlayerNameTv3.text =
                getString(R.string.player_name_cards, playerNames[5], cardsHeldNo[5].toString())
            mSamePlayerNameTv1.text =
                getString(R.string.player_name_cards, playerNames[2], cardsHeldNo[2].toString())
            mSamePlayerNameTv2.text =
                getString(R.string.player_name_cards, playerNames[4], cardsHeldNo[4].toString())
        }
    }

    private fun getLogsText(logs: List<TransactionLogVM>): String {
        if (logs.isEmpty()) return TextUtil.EMPTY
        val strBuilder = StringBuilder()
        logs.indices.forEach { count ->
            val log = logs[count]
            if (count > 0) strBuilder.append(TextUtil.NEWLINE)
            val askedBy = if (PlayGamePresenter.YOU.equals(log.askedBy)) {
                getResources().getString(R.string.you)
            } else {
                log.askedBy
            }
            val askedFrom = if (PlayGamePresenter.YOU.equals(log.askedFrom)) {
                getResources().getString(R.string.you)
            } else {
                log.askedFrom
            }
            val logTemplateId = if (log.wasSuccessful) {
                R.string.log_template_took
            } else {
                R.string.log_template_ask
            }
            strBuilder.append(
                getResources().getString(
                    logTemplateId,
                    askedBy,
                    askedFrom,
                    log.askedFor
                )
            )
        }
        return strBuilder.toString()
    }

    private fun updateCardsInHand(yourCards: List<String>) {
        mYourCardsLl.removeAllViews()
        val lpMargin = getCardsMargin(yourCards.size)
        yourCards.indices.forEach { cardCount ->
            val apiCardName = yourCards[cardCount]
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
            if (cardCount < yourCards.size - 1) { // don't reduce right margin for last card
                val lp = LinearLayout.LayoutParams(
                    Utils.getDimen(R.dimen.playing_card_width).toInt(),
                    Utils.getDimen(R.dimen.playing_card_height).toInt()
                )
                lp.setMargins(0, 0, lpMargin, 0) //Not RTL supported
                view.layoutParams = lp
                mYourCardsLl.addView(view)
            }
        }
    }

    private fun getCardsMargin(size: Int): Int {
        val min = Utils.getDimen(R.dimen.standard_in_hand_collapsed_margin).toInt()
        val max = Utils.getDimen(R.dimen.standard_margin_half).toInt()
        if (size <= 4) {
            return max
        }
        if (size >= 9) {
            return min
        }

        val margin = max + ((min - max) / 10 * (size - 4))
        if (margin < min) {
            return min
        }
        return margin
    }

    private fun getDroppedSetsText(droppedSets: List<String>): String {
        val str = StringBuilder(getString(R.string.dropped_sets))
        droppedSets.indices.forEach { setCount ->
            val setApiName = droppedSets[setCount]
            if (setCount > 0) {
                str.append(TextUtil.PIPE_SEPARATOR)
            }
            val setNameMap = SetNamesUtil.getMapping()
            if (setNameMap.containsKey(setApiName)) {
                str.append(getString(setNameMap.get(setApiName)!!))
            } else {
                str.append(setApiName)
            }
        }
        return str.toString()
    }

    private fun getSetDisplayNames(setApiNames: List<String>): List<String> {
        return getSetDisplayNames(setApiNames.toTypedArray())
    }

    private fun getSetDisplayNames(setApiNames: Array<String>): List<String> {
        val displayNames = ArrayList<String>()
        setApiNames.indices.forEach { setCount ->
            val setApiName = setApiNames[setCount]
            val setDisplayId = SetNamesUtil.getMapping().get(setApiName)
            if (setDisplayId != null) displayNames.add(getString(setDisplayId))
        }
        return displayNames
    }

    private fun getCardDisplayNames(cardApiNames: List<String>): List<String> {
        return getCardDisplayNames(cardApiNames.toTypedArray())
    }

    private fun getCardDisplayNames(cardApiNames: Array<String>): List<String> {
        val displayNames = ArrayList<String>()
        cardApiNames.indices.forEach { cardCount ->
            val cardApiName = cardApiNames[cardCount]
            val cardDisplayId = CardNamesUtil.getMapping().get(cardApiName)
            if (cardDisplayId != null) displayNames.add(getString(cardDisplayId))
        }
        return displayNames
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
        mHandler.postDelayed({
            mPresenter.stop()
        }, delayedTimeMillis)
    }

    private fun showAskDialog() {
        showAskDialog(null)
    }

    private fun showAskDialog(playerName: String?) {
        val layout = LayoutInflater.from(context).inflate(R.layout.ask_lit_dialog, null)
        val builder = AlertDialog.Builder(context!!)

        val title = layout.findViewById<TextView>(R.id.ask_title)
        val positiveBtn = layout.findViewById<View>(R.id.ask_positive_button)
        val negativeBtn = layout.findViewById<View>(R.id.ask_negative_button)
        val errorMessageTv = layout.findViewById<View>(R.id.ask_error_msg_tv)
        val askFromDropdown = layout.findViewById<Spinner>(R.id.ask_from_dropdown)
        val askForSetDropdown = layout.findViewById<Spinner>(R.id.ask_suit_dropdown)
        val askForCardDropdown = layout.findViewById<Spinner>(R.id.ask_card_dropdown)

        var oppositeTeamPlayerNames = mPresenter.getOppositeTeamPlayerNames()
        val askableSetNames = mPresenter.getAskableSetNames()
        var askableCards: List<String>? = null
        title.text = (getString(R.string.ask_dialog_title))

        if (playerName != null && oppositeTeamPlayerNames.contains(playerName)) {
            var selectedNameTop: MutableList<String>
            selectedNameTop = oppositeTeamPlayerNames as ArrayList<String>
            selectedNameTop.remove(playerName)
            selectedNameTop.add(0, playerName)
            oppositeTeamPlayerNames = selectedNameTop
        }

        askFromDropdown.setAdapter(
            ArrayAdapter(
                context!!,
                android.R.layout.simple_spinner_dropdown_item,
                oppositeTeamPlayerNames
            )
        )

        askForSetDropdown.setAdapter(
            ArrayAdapter(
                context!!,
                android.R.layout.simple_spinner_dropdown_item,
                getSetDisplayNames(askableSetNames)
            )
        )

        askForSetDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                askableCards =
                    mPresenter.getAskableCards(askableSetNames[askForSetDropdown.selectedItemPosition])
                askForCardDropdown.setAdapter(
                    ArrayAdapter(
                        context!!,
                        android.R.layout.simple_spinner_dropdown_item,
                        getCardDisplayNames(askableCards!!)
                    )
                )
                askForCardDropdown.visibility = VISIBLE
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                askForCardDropdown.visibility = GONE
            }
        }

        askForCardDropdown.visibility = GONE

        builder.setView(layout)

        var alertDialog: AlertDialog? = null

        alertDialog = builder.show()
        alertDialog.window?.decorView?.background = resources.getDrawable(R.drawable.dojo_dialog)

        // Set up the buttons
        positiveBtn.setOnClickListener {
            if (askableCards != null && askFromDropdown.selectedItem != null
                && askForSetDropdown != null && askForCardDropdown != null
            ) {
                mPresenter.askCard(
                    askFromDropdown.selectedItem as String,
                    askableCards!![askForCardDropdown.selectedItemPosition]
                )
                alertDialog?.dismiss()
            } else {
                errorMessageTv.visibility = View.VISIBLE
            }
        }
        negativeBtn.setOnClickListener {
            alertDialog?.dismiss()
        }

    }

    private fun showTransferDialog() {
        val layout = LayoutInflater.from(context).inflate(R.layout.transfer_lit_dialog, null)
        val builder = AlertDialog.Builder(context!!)

        val title = layout.findViewById<TextView>(R.id.transfer_title)
        val positiveBtn = layout.findViewById<View>(R.id.transfer_positive_button)
        val negativeBtn = layout.findViewById<View>(R.id.transfer_negative_button)
        val errorMessageTv = layout.findViewById<View>(R.id.transfer_error_msg_tv)
        val transferToDropdown = layout.findViewById<Spinner>(R.id.transfer_to_dropdown)
        val transferablePlayerNames = mPresenter.getTransferablePlayerNames()
        title.text = (getString(R.string.transfer_dialog_title))

        transferToDropdown.setAdapter(
            ArrayAdapter(
                context!!,
                android.R.layout.simple_spinner_dropdown_item,
                transferablePlayerNames
            )
        )

        builder.setView(layout)

        var alertDialog: AlertDialog? = null

        alertDialog = builder.show()
        alertDialog.window?.decorView?.background = resources.getDrawable(R.drawable.dojo_dialog)

        // Set up the buttons
        positiveBtn.setOnClickListener {
            if (transferToDropdown.selectedItem != null) {
                mPresenter.transferTurn(transferToDropdown.selectedItem as String)
                alertDialog?.dismiss()
            } else {
                errorMessageTv.visibility = View.VISIBLE
            }
        }
        negativeBtn.setOnClickListener {
            alertDialog?.dismiss()
        }

    }

    private fun showDeclareSetDialog() {
        val layout = LayoutInflater.from(context).inflate(R.layout.declare_lit_dialog, null)
        val builder = AlertDialog.Builder(context!!)

        val title = layout.findViewById<TextView>(R.id.declare_title)
        val declareSetDropdown = layout.findViewById<Spinner>(R.id.declare_set_dropdown)
        val player1NameTv = layout.findViewById<TextView>(R.id.player_1_name_tv)
        val player2NameTv = layout.findViewById<TextView>(R.id.player_2_name_tv)
        val player3NameTv = layout.findViewById<TextView>(R.id.player_3_name_tv)
        val player1CardsLl = layout.findViewById<DroppableLinearLayout>(R.id.player_1_cards_ll)
        val player2CardsLl = layout.findViewById<DroppableLinearLayout>(R.id.player_2_cards_ll)
        val player3CardsLl = layout.findViewById<DroppableLinearLayout>(R.id.player_3_cards_ll)
        val player1CardsApiNameList: MutableList<String> = ArrayList()
        val player2CardsApiNameList: MutableList<String> = ArrayList()
        val player3CardsApiNameList: MutableList<String> = ArrayList()
        val cardsLlTbl = layout.findViewById<TableLayout>(R.id.cards_ll_tbl)
        val askableSets = mPresenter.getAskableSetNames()
        val teamPlayerNames = mPresenter.getSameTeamPlayerNames(true)
        val positiveBtn = layout.findViewById<View>(R.id.declare_positive_button)
        val negativeBtn = layout.findViewById<View>(R.id.declare_negative_button)
        val errorMessageTv = layout.findViewById<View>(R.id.declare_error_msg_tv)
        title.text = (getString(R.string.declare_dialog_title))

        player1CardsLl.setup()
        player2CardsLl.setup()
        player3CardsLl.setup()

        player1NameTv.text = teamPlayerNames[0]
        player2NameTv.text = teamPlayerNames[1]
        player3NameTv.text = teamPlayerNames[2] // fixme for 4 players

        declareSetDropdown.setAdapter(
            ArrayAdapter(
                context!!,
                android.R.layout.simple_spinner_dropdown_item,
                getSetDisplayNames(askableSets)
            )
        )

        declareSetDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                cardsLlTbl.visibility = GONE
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                player1CardsLl.removeAllViews()
                player2CardsLl.removeAllViews()
                player3CardsLl.removeAllViews()
                val cardsForSelectedSet =
                    mPresenter.getCardsForSet(askableSets[declareSetDropdown.selectedItemPosition])
                val cardsDisplayName = getCardDisplayNames(cardsForSelectedSet)
                // TODO make LinearLayout implement drag ended
                cardsDisplayName.indices.forEach {
                    val tv = DraggableTextView(
                        this@PlayGameFragment.context,
                        null,
                        cardsDisplayName[it],
                        cardsForSelectedSet[it]
                    )
                    tv.setup()
                    val padding = Utils.getDimen(R.dimen.standard_padding_half).toInt()
                    tv.setPadding(padding, padding, padding, padding)
//                    tv.setSheetAttrs() // TODO
                    player1CardsLl.addView(tv)
                }
                cardsLlTbl.visibility = VISIBLE
            }
        }

        builder.setView(layout)

        var alertDialog: AlertDialog? = null

        alertDialog = builder.show()
        alertDialog.window?.decorView?.background = resources.getDrawable(R.drawable.dojo_dialog)

        // Set up the buttons
        positiveBtn.setOnClickListener {
            if (declareSetDropdown.selectedItem != null) {
                player1CardsLl.children.forEach {
                    player1CardsApiNameList.add((it as Draggable).getIdentifier())
                }
                player2CardsLl.children.forEach {
                    player2CardsApiNameList.add((it as Draggable).getIdentifier())
                }
                player3CardsLl.children.forEach {
                    player3CardsApiNameList.add((it as Draggable).getIdentifier())
                }

//                Utils.makeToastLong("declare clicked")
                mPresenter.dropSet(
                    player1CardsApiNameList,
                    player2CardsApiNameList,
                    player3CardsApiNameList
                )
                alertDialog?.dismiss()
            } else {
                errorMessageTv.visibility = View.VISIBLE
            }
        }
        negativeBtn.setOnClickListener {
            alertDialog?.dismiss()
        }

    }
}