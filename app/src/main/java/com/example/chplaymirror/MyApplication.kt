package com.example.chplaymirror

import android.app.Application
import com.google.android.gms.ads.MobileAds


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {
            /*Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()*/
        }
    }
}