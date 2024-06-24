package com.example.ocean.domain.storage

import androidx.datastore.preferences.core.booleanPreferencesKey

object DataStoreKey {
    val KEY_IS_COUNTRY_FLAG_IMAGES_DOWNLOADED =
        booleanPreferencesKey("is_country_flag_images_downloaded")
}