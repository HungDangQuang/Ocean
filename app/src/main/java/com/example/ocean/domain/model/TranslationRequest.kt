package com.example.ocean.domain.model

data class TranslationRequest(
    val text: String,
    val sourceLanguageCode: String,
    val targetLanguageCode: String
)
