package com.example.ocean.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Country
import com.example.ocean.data.repository.LocalStorageRepositoryImpl
import com.example.ocean.domain.repository.LocalStorageRepository
import kotlinx.coroutines.launch

class CountryListViewModel(
    private val localStorageRepository: LocalStorageRepository? = null
) : ViewModel() {

    private val _items = MutableLiveData<MutableList<Country>?>()
    val items: MutableLiveData<MutableList<Country>?> = _items

    private val _currentInputCountry = MutableLiveData<String?>()
    val currentInputCountry: LiveData<String?> = _currentInputCountry

    private val _currentOutputCountry = MutableLiveData<String?>()
    val currentOutputCountry: LiveData<String?> = _currentOutputCountry

    private val _isSelectingInputLanguage = MutableLiveData<Boolean>()
    val isSelectingInputLanguage: LiveData<Boolean> = _isSelectingInputLanguage

    companion object {
        private val TAG = CountryListViewModel::class.java.simpleName
    }

    init {
        fetchCountryList()
    }

    private fun fetchCountryList() {
        Log.d(TAG, "fetchCountryList")
        viewModelScope.launch {
            val data = localStorageRepository?.loadListOfCountries()
            _items.postValue(data)
        }
    }

    fun setCurrentCountry(countryName: String) {
        val targetCountry =
            if (_isSelectingInputLanguage.value == true) _currentInputCountry else _currentOutputCountry
        targetCountry.value = countryName
    }

    fun setIsSelectingInputLanguage(value: Boolean) {
        Log.d(TAG, "setIsSelectingInputLanguage")
        _isSelectingInputLanguage.value = value
    }
}