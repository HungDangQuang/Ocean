package com.example.ocean

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OceanApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance:OceanApplication? = null
        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(ActivityLifecycleHandler())
        FirebaseApp.initializeApp(this)
    }
}