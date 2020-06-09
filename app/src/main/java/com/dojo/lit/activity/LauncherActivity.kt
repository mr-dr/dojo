package com.dojo.lit.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.dojo.lit.R
import com.dojo.lit.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

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
