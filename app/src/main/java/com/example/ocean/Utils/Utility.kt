package com.example.ocean.Utils

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.ocean.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
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
                Log.d(TAG, "getCountries: $countryCode")
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

        fun getListOfCountryCode(): Array<String> = Locale.getISOCountries()

        fun saveImageToDisk(context: Context, byteArray: ByteArray, fileName: String) {
            val countryFlagFolder = File(context.filesDir, "country_flags")
            countryFlagFolder.takeIf { !it.exists() }?.apply { mkdirs() }
            val file = File(countryFlagFolder, "$fileName.png")
            // Write the byte array to the file
            try {
                FileOutputStream(file).use { fos ->
                    fos.write(byteArray)
                }
                Log.d(TAG, "Image $fileName.png saved successfully")
            } catch (e: IOException) {
                Log.e(TAG, "Failed to save image: ${e.message}")
            }
        }
    }
}