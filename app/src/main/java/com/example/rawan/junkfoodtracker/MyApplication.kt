package com.example.rawan.junkfoodtracker

import android.app.Application
import com.facebook.stetho.Stetho

/**
 * Created by rawan on 22/10/18.
 */
class MyApplication:Application(){
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
    }
}