package com.example.ocean.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ocean.domain.repository.LocalStorageRepository

class CountryListViewModelFactory(
    private val localStorageRepository: LocalStorageRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountryListViewModel::class.java))
            return CountryListViewModel(
                localStorageRepository
            ) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}