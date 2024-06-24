package com.example.ocean.data.repository.datasource

import androidx.datastore.preferences.core.Preferences

interface LocalDataStore {
    suspend fun <T> getValue(key: Preferences.Key<T>, defaultValue: T): T
    suspend fun <T> setValue(key: Preferences.Key<T>, value: T)
}