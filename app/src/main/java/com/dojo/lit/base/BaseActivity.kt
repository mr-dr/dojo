package com.dojo.lit.base

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dojo.lit.R
import com.dojo.lit.Utils
import com.dojo.lit.fragment.BaseFragment
import com.dojo.lit.util.TextUtil
import com.google.firebase.analytics.FirebaseAnalytics

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var progressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
//        setContentView(R.layout.activity_main)
    }

    final fun sendFirebaseEvent(event: String, bundle: Bundle) {
//        val bundle = Bundle()
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
//        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        firebaseAnalytics.logEvent(event, bundle)
    }

    // fixme : extremely important test this
    fun showUpdateDialog() {
//        val layout = LayoutInflater.from(baseContext).inflate(R.layout.update_app, null)
        val progressModal = AlertDialog.Builder(baseContext)
        progressModal.setTitle(R.string.update_app)

//        progressModal.setView(layout)

        // Set up the buttons
        progressModal.setPositiveButton(R.string.update_caps) { dialog, which ->
            // TODO add playstore link
        }
        progressModal.setNegativeButton(R.string.cancel_caps, { dialog, which -> finishAffinity() })
        progressModal.setCancelable(false)
        progressModal.show()
    }

    fun showProgressDialog() {
        val layout = LayoutInflater.from(baseContext).inflate(R.layout.progress, null)
        val progressModal = AlertDialog.Builder(baseContext)
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

    fun replaceFragment(
        fragment: BaseFragment,
        id: Int,
        addToBackStack: Boolean,
        animate: Boolean,
        backStackName: String
    ) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

//        if (animate) {
//            fragmentTransaction.setCustomAnimations(
//                R.anim.fragment_enter,
//                R.anim.fragment_exit,
//                R.anim.fragment_pop_back_in,
//                R.anim.fragment_pop_out
//            )
//        }

        fragmentTransaction.replace(id, fragment, backStackName)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(backStackName)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }

    fun showDojoToast(toastMessage: String, toastDuration: Int) {
        val inflater = layoutInflater
//        val container: ViewGroup = findViewById(R.id.custom_toast_container)
        val layout = inflater.inflate(R.layout.custom_toast, null)
        val text: TextView = layout.findViewById(R.id.text)
        text.text = toastMessage
        with (Toast(applicationContext)) {
            setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            duration = toastDuration
            view = layout
            show()
            Utils.makeNotificationSound()
        }
    }
}
