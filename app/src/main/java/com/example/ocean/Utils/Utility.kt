package com.example.ocean.Utils

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.ocean.R

class Utility {

    companion object {

        private val TAG = Utility::class.java.simpleName

        fun showExitConfirmationDialog(activity:Activity) {

            Log.d(TAG, "showExitConfirmationDialog")

            activity.let {
                AlertDialog.Builder(it).apply {
                    setTitle(it.getString(R.string.exit_confirmation_dialog_title))
                    setMessage(it.getString(R.string.exit_confirmation_dialog_message))
                    setPositiveButton(it.getString(R.string.common_button_yes)) { dialog, _ ->
                        dialog.dismiss()
                        Log.d(TAG, "showExitConfirmationDialog: quit app")
                        it.finish()
                    }
                    setNegativeButton(it.getString(R.string.common_button_no)) { dialog, _ ->
                        Log.d(TAG, "showExitConfirmationDialog: dismiss dialog")
                        dialog.dismiss()
                    }
                }.show()
            }
        }
    }
}