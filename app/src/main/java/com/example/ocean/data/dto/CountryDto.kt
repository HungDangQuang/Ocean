package com.example.ocean.data.dto

import com.example.domain.model.Country

class CountryDto(
    private val countryFlag: Int,
    private val countryName: String
) {
    fun toCountry() = Country(countryFlag, countryName)
}