package com.dojo.lit.lit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dojo.lit.R
import com.dojo.lit.fragment.BaseFragment
import com.dojo.lit.lit.GameInteractor
import android.text.InputType
import android.text.TextUtils
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.android.volley.VolleyError
import com.dojo.lit.Utils
import com.dojo.lit.base.BaseActivity
import com.dojo.lit.lit.BundleArgumentKeys
import com.dojo.lit.lit.activity.LitGameActivity
import com.dojo.lit.lit.model.CreateRoomResponse
import com.dojo.lit.network.ApiListeners
import com.dojo.lit.util.TextUtil




class InitGameFragment : BaseFragment() {

    lateinit var interactor: GameInteractor
    lateinit var welcomeTv: TextView
    lateinit var createRoomTv: TextView
    lateinit var joinRoomTv: TextView

    var logsDialogInput: String? = null
    var gameIdDialogInput: String? = null
    var aliasDialogInput: String? = null
    var playerNoDialogInput: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupInteractor()
        return inflater.inflate(R.layout.fragment_init_lit, container, false)
    }

    private fun setupInteractor() {
        interactor = GameInteractor()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        welcomeTv = findViewById(R.id.welcome_text)
        createRoomTv = findViewById(R.id.create_room_tv)
        joinRoomTv = findViewById(R.id.join_room_tv)
    }

    private fun setupClickListeners() {
        createRoomTv.setOnClickListener {
            showDialog(
                DialogTypes.CREATE_ROOM,
                getString(R.string.create_room_dialog_text),
                Runnable { makeCreateRoomCall() })
        }
        joinRoomTv.setOnClickListener {
            showDialog(
                DialogTypes.JOIN_ROOM,
                getString(R.string.join_room_dialog_text),
                Runnable { makeJoinRoomCall() })
        }
    }

    private fun makeCreateRoomCall() {
        showProgressDialog()
        interactor.createRoom(Integer.parseInt(logsDialogInput!!), object: ApiListeners<CreateRoomResponse>() {
            override fun onResponse(response: CreateRoomResponse?) {
                if (response == null || TextUtil.isEmpty(response.gameId)) {
                    onErrorResponse(VolleyError("Empty create room response"))
                    return
                }
                gameIdDialogInput = response.gameId
                hideProgressDialog()
                makeJoinRoomCall()
            }

            override fun onErrorResponse(error: VolleyError?) {
                hideProgressDialog()
                // TODO Log all errors
                Utils.makeToastLong("Room could not be created!")
            }

        })
    }

    private fun makeJoinRoomCall() {
        showProgressDialog()
        interactor.joinRoom(aliasDialogInput!!, gameIdDialogInput!!, playerNoDialogInput!!.toInt(), object: ApiListeners<String>() {
            override fun onResponse(response: String?) {
                hideProgressDialog()
                if (activity is LitGameActivity) {
                    val gameData = Bundle()
                    gameData.putString(BundleArgumentKeys.GAME_CODE, gameIdDialogInput)
                    gameData.putString(BundleArgumentKeys.ALIAS, aliasDialogInput)
                    gameData.putInt(BundleArgumentKeys.PLAYER_NO, playerNoDialogInput!!.toInt())
                    (activity as LitGameActivity).joinGame(gameData)
                } else {
                    Utils.makeToastLong("Something went wrong!")
                }
            }

            override fun onErrorResponse(error: VolleyError?) {
                hideProgressDialog()
                // TODO Log all errors
                Utils.makeToastLong("Room could not be joined!")
            }

        })
    }

    private fun showDialog(dialogType: String, title: String, confirmCallback: Runnable) {
        val layout = LayoutInflater.from(context).inflate(R.layout.init_lit_dialog, null)
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(title)

        val logsCountTv = layout.findViewById<EditText>(R.id.logs_count_tv)
        val gameIdTv = layout.findViewById<EditText>(R.id.game_code_tv)
        val aliasTv = layout.findViewById<EditText>(R.id.alias_tv)
        val playerNoTv = layout.findViewById<EditText>(R.id.player_no_tv)
        val errorMessageTv = layout.findViewById<View>(R.id.error_msg_tv)

        if(DialogTypes.CREATE_ROOM == dialogType) { // create room - logs count
            gameIdTv.visibility = View.GONE
        } else { // join room - game id
            logsCountTv.visibility = View.GONE
        }

        builder.setView(layout)

        // Set up the buttons
        builder.setPositiveButton(getString(R.string.confirm)) { dialog, which ->
            logsDialogInput = logsCountTv.text.toString()
            gameIdDialogInput = gameIdTv.text.toString()
            aliasDialogInput = aliasTv.text.toString()
            playerNoDialogInput = playerNoTv.text.toString()
            if((DialogTypes.CREATE_ROOM == dialogType && TextUtils.isEmpty(logsDialogInput))
                || (DialogTypes.JOIN_ROOM == dialogType && TextUtils.isEmpty(gameIdDialogInput))
                || TextUtils.isEmpty(aliasDialogInput)
                || TextUtils.isEmpty(playerNoDialogInput)) {
                errorMessageTv.visibility = View.VISIBLE
                return@setPositiveButton
            }
            confirmCallback.run()
        }
        builder.setNegativeButton(getString(R.string.cancel), { dialog, which -> dialog.cancel() })

        builder.show()
    }

    private object DialogTypes {
        val CREATE_ROOM = "create_room"
        val JOIN_ROOM = "join_room"
    }

}