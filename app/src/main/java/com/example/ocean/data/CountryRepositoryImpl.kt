package com.example.ocean.data

import com.example.domain.repository.CountryRepository
import com.example.ocean.data.service.CountryApiService

import okhttp3.ResponseBody
import retrofit2.Response

class CountryRepositoryImpl(private val apiService: CountryApiService):CountryRepository {

    override suspend fun getCountry(countryCode: String): Response<ResponseBody> {
        return apiService.getCountry(countryCode)
    }

}