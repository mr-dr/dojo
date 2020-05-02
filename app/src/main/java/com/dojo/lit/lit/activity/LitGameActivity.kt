package com.dojo.lit.lit.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.dojo.lit.R
import com.dojo.lit.base.BaseActivity
import com.dojo.lit.lit.fragment.PlayGameFragment

class LitGameActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lit)
        val playGameFragment = PlayGameFragment()
        // fixme
        replaceFragment(playGameFragment, R.id.content_frame, false, false, "LitGameActivity")
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
