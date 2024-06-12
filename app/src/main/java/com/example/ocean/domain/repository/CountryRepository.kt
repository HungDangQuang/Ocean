package com.example.domain.repository

import com.example.domain.model.Country
import okhttp3.ResponseBody
import retrofit2.Response

interface CountryRepository {
    suspend fun getCountry(countryCode: String): Response<ResponseBody>
}