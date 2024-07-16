package com.example.ocean.domain.repository

interface TranslationRepository {
    suspend fun translate(text: String, sourceLanguage: String ,targetLanguage: String): String
}