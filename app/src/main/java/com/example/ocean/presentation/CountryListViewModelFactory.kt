package com.example.ocean.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ocean.domain.repository.LocalStorageRepository
import com.example.ocean.domain.usecase.GetCurrentCountryUseCase
import com.example.ocean.domain.usecase.StoreCurrentCountryUseCase

class CountryListViewModelFactory(
    private val localStorageRepository: LocalStorageRepository? = null,
    private val getCurrentInputCountryUseCase: GetCurrentCountryUseCase,
    private val storeCurrentInputCountryUseCase: StoreCurrentCountryUseCase,
    private val getCurrentOutputCountryUseCase: GetCurrentCountryUseCase,
    private val storeCurrentOutputCountryUseCase: StoreCurrentCountryUseCase,

    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountryListViewModel::class.java))
            return CountryListViewModel(
                localStorageRepository,
                getCurrentInputCountryUseCase,
                storeCurrentInputCountryUseCase,
                getCurrentOutputCountryUseCase,
                storeCurrentOutputCountryUseCase
            ) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}