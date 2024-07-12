package com.example.ocean.data.repository

import android.util.Log
import com.example.ocean.data.service.TranslationApiService
import com.example.ocean.domain.repository.TranslationRepository

class TranslationRepositoryImpl(
    private val apiService: TranslationApiService
): TranslationRepository {
    override suspend fun translate(
        text: String,
        sourceLanguage: String,
        targetLanguage: String
    ): String {
        val result = apiService.translate(text, sourceLanguage, targetLanguage)
        return if (result.isSuccessful && result.code() == 200) {
            result.body()!!.response.translated_text
        } else {
            ""
        }
    }
}