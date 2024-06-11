package com.example.ocean.data.service

import com.example.ocean.data.dto.CountryDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryApiService {
    // todo: modify country code later
    @GET("/flag/AF.svg")
    suspend fun getCountries():List<CountryDto>

    @GET("/{country_code}/shiny/64.png")
    suspend fun getCountry(@Path("country_code") countryCode: String): Response<ResponseBody>
}