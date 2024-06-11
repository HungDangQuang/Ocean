package com.example.ocean

import android.util.Log
import androidx.databinding.ktx.BuildConfig

class Logger(private val TAG: String){
    private val priority:Int = Log.DEBUG

    public fun log(message:String) : Logger {
        if (BuildConfig.DEBUG){
            Log.println(priority,TAG,message)
        }
        return this
    }

    public fun withCause(cause:Exception){
        if (BuildConfig.DEBUG){
            Log.println(priority,TAG,Log.getStackTraceString(cause))
        }
    }
}