package com.example.ocean.data.repository

import androidx.datastore.preferences.core.Preferences
import com.example.ocean.data.repository.datasource.DataStorePreferences
import com.example.ocean.domain.repository.DataStoreRepository

class DataStoreRepositoryImpl(
    private val dataStorePreferences: DataStorePreferences
) : DataStoreRepository {
    override suspend fun setIsCountryFlagImagesDownloaded(
        key: Preferences.Key<Boolean>,
        defaultValue: Boolean
    ) {
        dataStorePreferences.setValue(key, defaultValue)
    }

    override suspend fun isCountryFlagImagesDownloaded(
        key: Preferences.Key<Boolean>,
        defaultValue: Boolean
    ): Boolean {
        return dataStorePreferences.getValue(key, defaultValue)
    }

}