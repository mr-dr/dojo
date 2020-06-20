package com.dojo.lit.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.dojo.lit.R
import com.dojo.lit.base.BaseActivity
import com.dojo.lit.lit.activity.LitGameActivity
import com.dojo.lit.lit.activity.LitInitActivity

// fixme skipping activity until more games
class GamesListActivity : BaseActivity() {
    private var tv: TextView? = null
    private var gameList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv = findViewById(R.id.start_button)
        tv?.text = "GamesList Screen\n" + tv?.text // fixme

        if (gameList.size <= 1) {
            val intent = Intent(this, LitInitActivity::class.java)
            startActivity(intent)
        }

    }


}
