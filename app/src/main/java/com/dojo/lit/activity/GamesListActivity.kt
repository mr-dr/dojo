package com.dojo.lit.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.dojo.lit.R
import com.dojo.lit.base.BaseActivity
import com.dojo.lit.lit.activity.LitGameActivity

class GamesListActivity : BaseActivity() {
    private var tv: TextView? = null
    private var gameList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv = findViewById(R.id.tv)
        tv?.text = "GamesList Screen\n" + tv?.text // fixme

        if (gameList.size <= 1) {
            val intent = Intent(this, LitGameActivity::class.java)
            startActivity(intent)
        }

    }
}
