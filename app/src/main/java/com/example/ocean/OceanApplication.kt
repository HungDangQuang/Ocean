package com.example.ocean

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OceanApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance:OceanApplication? = null
        lateinit var logger:Logger

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        logger = Logger(getString(R.string.app_name))
        registerActivityLifecycleCallbacks(ActivityLifecycleHandler())
    }
}