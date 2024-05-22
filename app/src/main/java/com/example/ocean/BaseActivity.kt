package com.example.ocean

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity(layoutResourceId: Int): AppCompatActivity(layoutResourceId) {

    private val TAG = StartUpActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }
}