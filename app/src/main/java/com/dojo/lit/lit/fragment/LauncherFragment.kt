package com.dojo.lit.lit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dojo.lit.R
import com.dojo.lit.fragment.BaseFragment
import android.widget.*
import androidx.appcompat.app.AlertDialog
import java.util.*
import android.os.Handler




class LauncherFragment : BaseFragment() {

    companion object {
        val TIME_OUT = 2000L
        val MSG_DISMISS_DIALOG = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        welcomeDialog()
        return View(context)
    }

    fun welcomeDialog()
    {
        val layout = LayoutInflater.from(context).inflate(R.layout.welcome_dialog, null)
        val builder = AlertDialog.Builder(context!!)
        builder.setView(layout)
        val noteList = Arrays.asList(R.string.welcome0, R.string.welcome1, R.string.welcome2, R.string.welcome3, R.string.welcome4,
            R.string.welcome5, R.string.welcome6, R.string.welcome7, R.string.welcome8, R.string.welcome9,
            R.string.welcome10,R.string.welcome11,R.string.welcome12,R.string.welcome13,R.string.welcome14,
            R.string.welcome15) //fixme move to presenter

        val noteNumber = (Math.random() * 100 % noteList.size).toInt()
        val mWelcomeTv = layout.findViewById<TextView>(R.id.welcome_note_tv)

        val note = noteList[noteNumber]
        mWelcomeTv.text = resources.getString(note)
        val alertDialog = builder.show()
        alertDialog.window?.decorView?.background = resources.getDrawable(R.drawable.dojo_dialog_welcome_note)
        val mHandler = object : Handler() {
            override fun handleMessage(msg: android.os.Message) {
                when (msg.what) {
                    MSG_DISMISS_DIALOG -> if (alertDialog != null && alertDialog.isShowing()) {
                        alertDialog.dismiss()
                    }

                    else -> {
                    }
                }
            }
        }
        mHandler.sendEmptyMessageDelayed(MSG_DISMISS_DIALOG, TIME_OUT)
    }

}