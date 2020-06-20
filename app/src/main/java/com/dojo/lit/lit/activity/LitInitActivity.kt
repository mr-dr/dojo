package com.dojo.lit.lit.activity

import android.os.Bundle
import com.dojo.lit.R
import com.dojo.lit.base.BaseActivity
import com.dojo.lit.lit.fragment.InitGameFragment

class LitInitActivity : BaseActivity() {

    lateinit var initGameFragment: InitGameFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lit)
        initGameFragment = InitGameFragment()
        replaceFragment(initGameFragment, R.id.content_frame, false, false, "LitInitActivity")

    }
}
