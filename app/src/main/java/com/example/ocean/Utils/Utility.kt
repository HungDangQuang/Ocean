package com.example.ocean.Utils

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.ocean.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Locale

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

        fun getCountries(): ArrayList<String> {
            val isoCountryCodes: Array<String> = Locale.getISOCountries()
            val countriesWithEmojis: ArrayList<String> = arrayListOf()
            for (countryCode in isoCountryCodes) {
//                Log.d(TAG, "getCountries: $countryCode")
                val locale = Locale("", countryCode)
                val countryName: String = locale.displayCountry
                val flagOffset = 0x1F1E6
                val asciiOffset = 0x41
                val firstChar = Character.codePointAt(countryCode, 0) - asciiOffset + flagOffset
                val secondChar = Character.codePointAt(countryCode, 1) - asciiOffset + flagOffset
                val flag =
                    (String(Character.toChars(firstChar)) + String(Character.toChars(secondChar)))
                countriesWithEmojis.add("$flag $countryName")
            }
            return countriesWithEmojis
        }

        fun saveImageToDisk(context: Context, byteArray: ByteArray) {
            Log.d(TAG, "saveImageToDisk")
            val file = File(context.filesDir, "downloaded_image.jpg")
            // Create the parent directories if they do not exist
            file.parentFile?.mkdirs()

            // Write the byte array to the file
            try {
                FileOutputStream(file).use { fos ->
                    fos.write(byteArray)
                }
                println("Image saved successfully")
            } catch (e: IOException) {
                e.printStackTrace()
                println("Failed to save image: ${e.message}")
            }
        }
    }
}