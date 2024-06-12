package com.example.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Country
import com.example.ocean.domain.usecase.GetCountryUseCase
import com.example.ocean.domain.usecase.Result
import kotlinx.coroutines.launch

class CountryViewModel(private val getCountryUseCase: GetCountryUseCase): ViewModel() {

    private val _countries = MutableLiveData<List<Country>>()
    val countries: LiveData<List<Country>> get() = _countries

    private val TAG = CountryViewModel::class.java.simpleName

    init {
        fetchCountries()
    }



    private fun fetchCountries() = viewModelScope.launch {
//        val countryList = getCountryUseCase.execute()
//        _countries.value = countryList
        val result = getCountryUseCase(Unit)
        if (result is Result.Success) {
            Log.d(TAG, "fetchCountries: successful result")
        } else {
            Log.d(TAG, "fetchCountries: failed result")
        }

    }
}