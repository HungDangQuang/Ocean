package com.example.ocean.domain.repository

import androidx.datastore.preferences.core.Preferences

interface DataStoreRepository {
    suspend fun setIsCountryFlagImagesDownloaded(
        key: Preferences.Key<Boolean>,
        defaultValue: Boolean
    )

    suspend fun isCountryFlagImagesDownloaded(
        key: Preferences.Key<Boolean>,
        defaultValue: Boolean
    ): Boolean

    suspend fun setCurrentCountryName(
        key: Preferences.Key<String>,
        defaultValue: String
    )

    suspend fun getCurrentCountryName(
        key: Preferences.Key<String>,
        defaultValue: String
    ): String
}