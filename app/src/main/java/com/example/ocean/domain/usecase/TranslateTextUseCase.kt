package com.example.ocean.domain.usecase

import android.util.Log
import com.example.ocean.domain.model.TranslationRequest
import com.example.ocean.domain.repository.TranslationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TranslateTextUseCase(
    private val translationRepository: TranslationRepository,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<TranslationRequest, String>(coroutineDispatcher) {
    val TAG = "TranslateTextUseCase"
    override suspend fun execute(parameters: TranslationRequest) : String {
        Log.d(TAG, "execute: ${parameters.text} ${parameters.sourceLanguageCode} ${parameters.targetLanguageCode}")
        return translationRepository.translate(
            parameters.text,
            parameters.sourceLanguageCode,
            parameters.targetLanguageCode
        )
    }
}