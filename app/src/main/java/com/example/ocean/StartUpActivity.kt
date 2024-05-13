package com.example.ocean

import android.util.Log
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class StartUpActivity : AppCompatActivity(R.layout.activity_start_up) {

    private val TAG = StartUpActivity::class.simpleName

    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {
        Log.d(TAG, "getOnBackInvokedDispatcher")
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        })
        return super.getOnBackInvokedDispatcher()
    }

    private fun showExitConfirmationDialog() {

        Log.d(TAG, "showExitConfirmationDialog")

        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.exit_confirmation_dialog_title))
            setMessage(getString(R.string.exit_confirmation_dialog_message))
            setPositiveButton(getString(R.string.common_button_yes)) { dialog, _ ->
                dialog.dismiss()
                Log.d(TAG, "showExitConfirmationDialog: quit app")
                finish()
            }
            setNegativeButton(getString(R.string.common_button_no)) { dialog, _ ->
                Log.d(TAG, "showExitConfirmationDialog: dismiss dialog")
                dialog.dismiss()
            }
        }.show()
    }

}