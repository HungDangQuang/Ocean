package com.example.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ocean.domain.usecase.GetImagesDownloadedFlagUseCase
import com.example.ocean.domain.usecase.StoreImagesDownloadedFlagUseCase

class CountryViewModelFactory(
    private val storeImagesDownloadedFlagUseCase: StoreImagesDownloadedFlagUseCase,
    private val getImagesDownloadedFlagUseCase: GetImagesDownloadedFlagUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountryViewModel::class.java))
            return CountryViewModel(
                storeImagesDownloadedFlagUseCase,
                getImagesDownloadedFlagUseCase
            ) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}