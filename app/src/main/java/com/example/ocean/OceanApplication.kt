package com.example.ocean

import android.app.Application

class OceanApplication : Application() {

    companion object {
        private lateinit var instance:OceanApplication
        lateinit var logger:Logger

        fun getInstance():OceanApplication {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        logger = Logger(getString(R.string.app_name))
        registerActivityLifecycleCallbacks(ActivityLifecycleHandler())
    }
}