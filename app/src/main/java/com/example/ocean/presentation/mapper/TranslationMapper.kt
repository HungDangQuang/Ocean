package com.example.ocean.presentation.mapper

import com.example.ocean.domain.model.TranslationRequest

class TranslationMapper {
    fun mapToDomain(requestDTO: TranslationRequestDTO): TranslationRequest {
        return TranslationRequest(
            requestDTO.text,
            requestDTO.sourceLanguageCode,
            requestDTO.targetLanguageCode
        )
    }
}