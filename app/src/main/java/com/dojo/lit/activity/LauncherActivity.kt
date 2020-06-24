package com.dojo.lit.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.android.volley.VolleyError
import com.dojo.lit.R
import com.dojo.lit.Utils
import com.dojo.lit.base.BaseActivity
import com.dojo.lit.lit.GameInteractor
import com.dojo.lit.lit.activity.LitInitActivity
import com.dojo.lit.lit.fragment.LauncherFragment
import com.dojo.lit.lit.model.AppInitResponse
import com.dojo.lit.network.ApiListeners

class LauncherActivity : BaseActivity() {
    private var tv: TextView? = null
    private lateinit var mInteractor: GameInteractor
    private lateinit var mLauncherFragment: LauncherFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInteractor = GameInteractor()
        setContentView(R.layout.activity_main)
//        showUpdateDialog()
        tv = findViewById(R.id.start_button)
        mLauncherFragment = LauncherFragment()
        replaceFragment(mLauncherFragment, R.id.content_frame, false, false, "LauncherActivity")

        tv?.setOnClickListener { view -> // temp impl
            mLauncherFragment.showProgressDialog()
            isUpgradeNeeded(object: ApiListeners<AppInitResponse>() {
                override fun onResponse(response: AppInitResponse?) {
                    mLauncherFragment.hideProgressDialog()
                    if (response?.forceUpgrade ?: false) {
                        mLauncherFragment.showForceUpgradeDialog()
                    } else {
                        val intent = Intent(this@LauncherActivity, LitInitActivity::class.java)
                        startActivity(intent)
                        this@LauncherActivity.finish()
                    }
                }

                override fun onErrorResponse(error: VolleyError?) {
                    mLauncherFragment.hideProgressDialog()
                    Utils.makeToastLong("Something went wrong!")
                }

            })
        }
    }

    private fun isUpgradeNeeded(listener: ApiListeners<AppInitResponse>) {
        mInteractor.forceUpgradeCheck(listener)
    }
}
