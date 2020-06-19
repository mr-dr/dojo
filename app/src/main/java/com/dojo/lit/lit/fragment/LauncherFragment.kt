package com.dojo.lit.lit.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dojo.lit.R
import com.dojo.lit.fragment.BaseFragment
import com.dojo.lit.lit.GameInteractor
import android.text.TextUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.volley.VolleyError
import com.dojo.lit.Utils
import com.dojo.lit.lit.BundleArgumentKeys
import com.dojo.lit.lit.activity.LitGameActivity
import com.dojo.lit.lit.activity.LitInitActivity
import com.dojo.lit.lit.model.CreateRoomResponse
import com.dojo.lit.network.ApiListeners
import com.dojo.lit.util.TextUtil
import com.dojo.lit.view.DojoAlertDialog
import com.dojo.lit.view.SheetHelper
import java.util.*


class LauncherFragment : BaseFragment() {

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

        val noteNumber = (Math.random() * 100 % 16).toInt()
        val mWelcomeTv = layout.findViewById<TextView>(R.id.welcome_note_tv)

        val note = noteList[noteNumber]
        mWelcomeTv.text = resources.getString(note)
        var alertDialog = builder.show()
        alertDialog.window?.decorView?.background = resources.getDrawable(R.drawable.dojo_dialog_welcome_note)
    }

}