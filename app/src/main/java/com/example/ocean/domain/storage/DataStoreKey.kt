package com.example.ocean.domain.storage

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKey {
    val KEY_IS_COUNTRY_FLAG_IMAGES_DOWNLOADED =
        booleanPreferencesKey("is_country_flag_images_downloaded")

    val KEY_CURRENT_INPUT_COUNTRY_NAME = stringPreferencesKey("current_input_country_name")

    val KEY_CURRENT_OUTPUT_COUNTRY_NAME = stringPreferencesKey("current_output_country_name")
}