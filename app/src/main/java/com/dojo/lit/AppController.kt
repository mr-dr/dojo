package com.dojo.lit

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.google.android.play.core.splitcompat.SplitCompatApplication

class AppController : SplitCompatApplication() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private var instance: AppController? = null
        fun getInstance(): AppController {
            return instance!!
        }
        fun getResources(): Resources {
            return getInstance().resources!!
        }
        fun getApplicationContext(): Context {
            return getInstance().applicationContext!!
        }
    }
}
