package com.example.ocean.data.repository

import android.graphics.BitmapFactory
import com.example.domain.model.Country
import com.example.ocean.OceanApplication
import com.example.ocean.domain.repository.LocalStorageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

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

}