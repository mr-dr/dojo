package com.dojo.lit.lit.activity

import android.os.Bundle
import com.dojo.lit.R
import com.dojo.lit.base.BaseActivity
import com.dojo.lit.lit.BundleArgumentKeys
import com.dojo.lit.lit.fragment.InitGameFragment
import com.dojo.lit.lit.fragment.PlayGameFragment

class LitGameActivity : BaseActivity() {

    lateinit var playGameFragment: PlayGameFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lit)
        playGameFragment = PlayGameFragment()
        playGameFragment.arguments = intent.extras!![BundleArgumentKeys.GAME_DATA_ARGS] as Bundle
        replaceFragment(playGameFragment, R.id.content_frame, false, false, "LitGameActivity")

    }

}
