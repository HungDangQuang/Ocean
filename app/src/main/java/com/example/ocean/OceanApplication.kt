package com.example.ocean

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.example.ocean.Utils.Constants.Companion.appName

class OceanApplication : Application() {

    companion object {
        var context: Context? = null
        val logger = Logger(appName)
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        registerActivityLifecycleCallbacks(ActivityLifecycleHandler(this))
    }
}