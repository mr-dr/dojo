package com.dojo.lit.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.dojo.lit.R
import com.dojo.lit.base.BaseActivity

class LauncherActivity : BaseActivity() {
    private var tv: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv = findViewById(R.id.tv)
        tv?.text = "Launcher Screen\n" + tv?.text // fixme

        tv?.setOnClickListener { view -> // temp impl
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            // TODO delete this activity after sending elsewhere
        }
    }
}
