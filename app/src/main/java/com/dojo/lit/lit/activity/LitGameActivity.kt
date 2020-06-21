package com.dojo.lit.lit.activity

import android.os.Bundle
import android.util.Log
import com.dojo.lit.R
import com.dojo.lit.base.BaseActivity
import com.dojo.lit.lit.BundleArgumentKeys
import com.dojo.lit.lit.fragment.PlayGameFragment

class LitGameActivity : BaseActivity() {

    val LOG_TAG = "play_lit_presenter"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_lit)
        val playGameFragment = PlayGameFragment()
        Log.d(LOG_TAG, "onCreate() activity called")
        playGameFragment.arguments = intent.extras!![BundleArgumentKeys.GAME_DATA_ARGS] as Bundle
        replaceFragment(playGameFragment, R.id.content_frame, false, false, "LitGameActivity")

    }

}
