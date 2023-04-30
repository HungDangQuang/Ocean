package com.example.ocean

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import kotlinx.coroutines.*
import java.util.TimerTask

class StartUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_up)

        runBlocking {
            GlobalScope.launch(){
                delay(3000L)
                startActivity(Intent(this@StartUpActivity,MainActivity::class.java))
            }
        }
    }
}