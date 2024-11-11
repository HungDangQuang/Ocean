package com.example.ocean.domain.repository

import com.example.domain.model.Country

interface LocalStorageRepository {
    suspend fun loadListOfCountries(): MutableList<Country>
    suspend fun storeImageFromUri(uri: String):String
}