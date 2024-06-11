package com.example.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.usecase.GetCountriesUseCase
import com.example.ocean.domain.usecase.GetCountryUseCase

class CountryViewModelFactory(private val getCountryUseCase: GetCountryUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountryViewModel::class.java))
            return CountryViewModel(getCountryUseCase) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}