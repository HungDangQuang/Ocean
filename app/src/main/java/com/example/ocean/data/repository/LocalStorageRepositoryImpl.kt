package com.example.ocean.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.domain.model.Country
import com.example.ocean.OceanApplication
import com.example.ocean.domain.repository.LocalStorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class LocalStorageRepositoryImpl : LocalStorageRepository {
    override suspend fun loadListOfCountries(): MutableList<Country> = withContext(Dispatchers.IO) {
        val list = mutableListOf<Country>()
        val countryFlagFolder =
            File(OceanApplication.applicationContext().filesDir, "country_flags")
        countryFlagFolder.listFiles()?.forEach { file ->
            list.add(
                Country(
                    BitmapFactory.decodeFile(file.absolutePath),
                    file.nameWithoutExtension
                )
            )
        }
        return@withContext list
    }

    override suspend fun storeImageFromUri(uri: String): String {
        val inputStream = java.net.URL(uri).openStream()
        val bitmap =  BitmapFactory.decodeStream(inputStream)
        val imageFolder = File(OceanApplication.applicationContext().filesDir, "user_profile")
        imageFolder.takeIf { !it.exists() }?.apply { mkdirs() }
        val file = File(imageFolder, "avatar.png")
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        out.flush()
        out.close()
        return file.absolutePath
    }


}