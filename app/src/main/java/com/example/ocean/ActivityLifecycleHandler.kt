package com.example.ocean

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import kotlin.math.log

class ActivityLifecycleHandler(private val application: Application) :
    Application.ActivityLifecycleCallbacks {

    companion object {
        private const val TAG = "LifecycleCallbacks"
    }

    private val logger = Logger(TAG)

    override fun onActivityPaused(p0: Activity) {
        logger.log("onActivityPaused at ${p0.localClassName}")
    }

    override fun onActivityStarted(p0: Activity) {
        logger.log("onActivityStarted at ${p0.localClassName}")
    }

    override fun onActivityDestroyed(p0: Activity) {
        logger.log("onActivityDestroyed at ${p0.localClassName}")
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        logger.log("onActivitySaveInstanceState at ${p0.localClassName}")
    }

    override fun onActivityStopped(p0: Activity) {
        logger.log("onActivityStopped at ${p0.localClassName}")
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        logger.log("onActivityCreated at ${p0.localClassName}")
    }

    override fun onActivityResumed(p0: Activity) {
        logger.log("onActivityCreated at ${p0.localClassName}")
    }

}