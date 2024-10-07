package com.example.ocean.Utils

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View.OnClickListener
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.ocean.OceanApplication
import com.example.ocean.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
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

        fun saveImageToDisk(byteArray: ByteArray, fileName: String) {
            val countryFlagFolder = File(OceanApplication.applicationContext().filesDir, "country_flags")
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

        fun showDialog(dialog: Dialog, uiResource: Int, positiveButtonUiResource: Int, negativeButtonUiResource: Int, dialogHandler: DialogHandler ) {

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(uiResource)

            dialog.window?.setLayout(
                (getDeviceWidth() * 0.9f).toInt(),
                LayoutParams.WRAP_CONTENT
            )

            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);

            val positiveButton = dialog.findViewById<Button>(positiveButtonUiResource)
            positiveButton.setOnClickListener(dialogHandler.onPositiveClick())

            val negativeButton = dialog.findViewById<Button>(negativeButtonUiResource)
            negativeButton.setOnClickListener(dialogHandler.onNegativeClick())

            dialog.show()
        }

        fun getDeviceWidth() = Resources.getSystem().displayMetrics.widthPixels
        fun getDeviceHeight() = Resources.getSystem().displayMetrics.heightPixels

        fun getLanguageCodeBasedOnName(language: String) : String {
            return Constants.supportedLanguageList.find { it.language == language }?.languageCode ?: ""
        }

        fun isPermissionGranted(context: Context, permission: String) : Boolean {
            return ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }

        fun copyTextToClipboard(text: String, context: Context) {

            // Get the Clipboard Manager
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            // Create a ClipData with the text to copy
            val clip = ClipData.newPlainText("ocr_text", text)

            // Set the ClipData into the ClipboardManager
            clipboard.setPrimaryClip(clip)
        }

        fun saveImage(bitmap: Bitmap, context: Context) : Uri? {
            val imageFolder = File(context.cacheDir, "images")
            var uri: Uri? = null
            try {
                imageFolder.mkdirs()
                val file = File(imageFolder, "ocr_images.jpg")
                val stream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()
                stream.close()
                uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            } catch (e: IOException){
                Log.e(TAG, "saveImage: error occurred: ${e.message}", null)
            }
            return uri
        }


        interface DialogHandler {
            fun onPositiveClick(): OnClickListener
            fun onNegativeClick(): OnClickListener
        }
    }
}