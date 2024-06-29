package com.example.ocean.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Country
import com.example.ocean.domain.repository.LocalStorageRepository
import kotlinx.coroutines.launch

class CountryListViewModel(
    private val localStorageRepository: LocalStorageRepository
) : ViewModel() {

    private val _items = MutableLiveData<MutableList<Country>>()
    val items: LiveData<MutableList<Country>> = _items

    companion object {
        private val TAG = CountryListViewModel::class.java.simpleName
    }

    init {
        fetchCountryList()
    }

    private fun fetchCountryList() {
        Log.d(TAG, "fetchCountryList")
        viewModelScope.launch {
            val data = localStorageRepository.loadListOfCountries()
            _items.postValue(data)
        }
    }
}