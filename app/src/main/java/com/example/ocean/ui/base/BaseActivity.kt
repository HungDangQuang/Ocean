package com.example.ocean.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ocean.ui.component.plash.StartUpActivity

open class BaseActivity(layoutResourceId: Int): AppCompatActivity(layoutResourceId) {

    private val TAG = StartUpActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }
}