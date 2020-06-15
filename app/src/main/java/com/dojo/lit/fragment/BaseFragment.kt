package com.dojo.lit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.dojo.lit.R
import com.dojo.lit.base.BaseActivity
import com.dojo.lit.util.TextUtil

abstract open class BaseFragment : Fragment() {
    private var progressDialog: AlertDialog? = null

    final fun sendFirebaseEvent(event: String, bundle: Bundle) {
        (activity as BaseActivity).sendFirebaseEvent(event, bundle)
    }

    fun <T : View> findViewById(id: Int): T {
        if (view == null) {
            throw NullPointerException("mNegative view attached to this fragment")
        }
        return view!!.findViewById(id)
    }

    fun showProgressDialog() {
        val layout = LayoutInflater.from(context).inflate(R.layout.progress, null)
        val progressModal = AlertDialog.Builder(context!!)
        progressModal.setTitle(TextUtil.EMPTY)

        progressModal.setView(layout)

        // Set up the buttons
        progressModal.setPositiveButton(TextUtil.EMPTY) { dialog, which ->
        }
        progressModal.setNegativeButton(TextUtil.EMPTY, { dialog, which -> dialog.cancel() })
        progressDialog = progressModal.show()
    }

    fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    fun showDojoToast(toastMessage: String, toastDuration: Int) {
        (activity as BaseActivity).showDojoToast(toastMessage, toastDuration)
    }
}
