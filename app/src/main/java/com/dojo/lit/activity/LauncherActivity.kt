package com.dojo.lit.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.dojo.lit.R
import com.dojo.lit.base.BaseActivity
import com.dojo.lit.lit.activity.LitInitActivity

class LauncherActivity : BaseActivity() {
    private var tv: TextView? = null
    private var welcomeTv: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        showUpdateDialog()
        tv = findViewById(R.id.start_button)
        welcomeTv = findViewById(R.id.welcome_note)

        //tv?.text = "Launcher Screen\n" + tv?.text // fixme
        welcomeTv?.text = getResources().getString(R.string.welcome1)

        tv?.setOnClickListener { view -> // temp impl
            val intent = Intent(this, LitInitActivity::class.java)
            startActivity(intent)
            // TODO delete this activity after sending elsewhere
        }
    }
}
