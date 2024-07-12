package com.example.ocean.data.service

import com.example.ocean.domain.model.TranslationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslationApiService {

    @GET("translate")
    suspend fun translate(
        @Query("text") text: String,
        @Query("source_lang") sourceLang: String,
        @Query("target_lang") targetLang: String
    ): Response<TranslationResponse>
}