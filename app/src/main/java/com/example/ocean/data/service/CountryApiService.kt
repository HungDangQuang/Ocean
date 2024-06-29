package com.example.ocean.data.service

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryApiService {

    @GET("/{country_code}/flat/64.png")
    suspend fun getCountry(@Path("country_code") countryCode: String): Response<ResponseBody>
}