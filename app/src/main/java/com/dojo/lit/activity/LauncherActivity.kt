package com.dojo.lit.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.dojo.lit.R
import com.dojo.lit.Utils
import com.dojo.lit.base.BaseActivity
import com.dojo.lit.lit.activity.LitInitActivity
import com.dojo.lit.lit.fragment.LauncherFragment
import java.util.*

class LauncherActivity : BaseActivity() {
    private var tv: TextView? = null
    private var welcomeTv: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        showUpdateDialog()
        tv = findViewById(R.id.start_button)
        val LauncherFragment = LauncherFragment()
        replaceFragment(LauncherFragment, R.id.content_frame, false, false, "LauncherActivity")

        tv?.setOnClickListener { view -> // temp impl
            val intent = Intent(this, LitInitActivity::class.java)
            startActivity(intent)
            // TODO delete this activity after sending elsewhere
        }
    }
}
