package com.example.ocean

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ocean.data.Country

class CountryViewModel: ViewModel() {

    val inputLanguageCountry:MutableLiveData<Country> by lazy {
        MutableLiveData<Country>()
    }

    val outputLanguageCountry:MutableLiveData<Country> by lazy {
        MutableLiveData<Country>()
    }
}