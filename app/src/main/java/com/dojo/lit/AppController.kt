package com.dojo.lit

import android.content.Context
import android.content.res.Resources
import com.google.android.play.core.splitcompat.SplitCompatApplication
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

class AppController : SplitCompatApplication() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        MobileAds.initialize(this, object : OnInitializationCompleteListener {
            override fun onInitializationComplete(initializationStatus: InitializationStatus) {}
        })
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
