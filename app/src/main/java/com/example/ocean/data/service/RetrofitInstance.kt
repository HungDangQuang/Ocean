package com.example.ocean.data.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// https://flagsapi.com/
// https://countryflagsapi.netlify.app/
object RetrofitInstance {
    val apiService: CountryApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://flagsapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountryApiService::class.java)
    }

    val translationService: TranslationApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://655.mtis.workers.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslationApiService::class.java)
    }
}