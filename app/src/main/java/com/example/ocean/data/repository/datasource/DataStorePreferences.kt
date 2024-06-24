package com.example.ocean.data.repository.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first


class DataStorePreferences(private val context: Context): LocalDataStore {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")

    override suspend fun <T> getValue(key: Preferences.Key<T>, defaultValue: T): T {
        val prefs = context.dataStore.data.first()
        return prefs[key] ?: defaultValue
    }

    override suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

}