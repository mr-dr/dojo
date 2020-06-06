package com.dojo.lit.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dojo.lit.R
import com.dojo.lit.fragment.BaseFragment
import com.dojo.lit.util.TextUtil
import com.google.firebase.analytics.FirebaseAnalytics

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var progressDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        setContentView(R.layout.activity_main)
    }

    final fun sendFirebaseEvent(event: String, bundle: Bundle) {
//        val bundle = Bundle()
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
//        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        firebaseAnalytics.logEvent(event, bundle)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
