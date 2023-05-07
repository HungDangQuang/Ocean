package com.example.ocean

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle

class OceanApplication : Application() {

    companion object {
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        registerActivityLifecycleCallbacks(ActivityLifecycleHandler(this))
    }
}