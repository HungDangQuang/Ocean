package com.example.ocean.presentation.mapper

data class TranslationRequestDTO(
    val text: String,
    val sourceLanguageCode: String,
    val targetLanguageCode: String
)
