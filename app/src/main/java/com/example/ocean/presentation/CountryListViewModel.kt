package com.example.ocean.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Country
import com.example.ocean.Utils.Utility
import com.example.ocean.domain.repository.LocalStorageRepository
import com.example.ocean.domain.usecase.GetCurrentCountryUseCase
import com.example.ocean.domain.usecase.Result
import com.example.ocean.domain.usecase.StoreCurrentCountryUseCase
import com.example.ocean.domain.usecase.TranslateTextUseCase
import com.example.ocean.presentation.mapper.TranslationMapper
import com.example.ocean.presentation.mapper.TranslationRequestDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class CountryListViewModel @Inject constructor(
    @Named("currentInputCountry") getCurrentInputCountryUseCase: GetCurrentCountryUseCase,
    @Named("storeCurrentInputCountry") private val storeCurrentInputCountryUseCase: StoreCurrentCountryUseCase,
    @Named("currentOutputCountry") getCurrentOutputCountryUseCase: GetCurrentCountryUseCase,
    @Named("storeCurrentOutputCountry") private val storeCurrentOutputCountryUseCase: StoreCurrentCountryUseCase,
    private val localStorageRepository: LocalStorageRepository,
    private val translationUseCase: TranslateTextUseCase
) : ViewModel() {

    private val _items = MutableLiveData<MutableList<Country>?>()
    val items: MutableLiveData<MutableList<Country>?> = _items

    private val _currentInputCountry = MutableLiveData<String?>()
    val currentInputCountry: LiveData<String?> = _currentInputCountry

    private val _currentOutputCountry = MutableLiveData<String?>()
    val currentOutputCountry: LiveData<String?> = _currentOutputCountry

    private val _isSelectingInputLanguage = MutableLiveData<Boolean>()
    val isSelectingInputLanguage: LiveData<Boolean> = _isSelectingInputLanguage

    private val _translatedText = MutableLiveData<String>()
    val translatedText: LiveData<String> = _translatedText

    companion object {
        private val TAG = CountryListViewModel::class.java.simpleName
    }

    init {
        fetchCountryList()
        getCurrentCountry(getCurrentInputCountryUseCase, _currentInputCountry)
        getCurrentCountry(getCurrentOutputCountryUseCase, _currentOutputCountry)
    }

    private fun fetchCountryList() {
        Log.d(TAG, "fetchCountryList")
        viewModelScope.launch {
            val data = localStorageRepository.loadListOfCountries()
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

    private fun getCurrentCountry (
        getCurrentCountryUseCase: GetCurrentCountryUseCase,
        currentCountry: MutableLiveData<String?>
    ) {
        Log.d(TAG, "getCurrentCountry")
        viewModelScope.launch {
            val currentCountryResult = getCurrentCountryUseCase(Unit)
            if (currentCountryResult is Result.Success) {
                Log.d(TAG, "getCurrentCountry: result is success")
                currentCountry.postValue(currentCountryResult.data)
            } else {

            }
        }
    }

    fun storeCurrentCountries() {
        viewModelScope.launch {
            storeCurrentInputCountryUseCase(_currentInputCountry.value!!)
            storeCurrentOutputCountryUseCase(_currentOutputCountry.value!!)
        }
    }

    fun translateText(text: String, callBack: ((String) -> Unit)? = null) {
        viewModelScope.launch {
            val translationRequestDto = TranslationRequestDTO(
                text,
                Utility.getLanguageCodeBasedOnName(_currentInputCountry.value!!),
                Utility.getLanguageCodeBasedOnName(_currentOutputCountry.value!!)
            )
            val request = TranslationMapper().mapToDomain(translationRequestDto)
            val result = translationUseCase(request)
            if (result is Result.Success) {
                callBack?.invoke(result.data) ?: _translatedText.postValue(result.data)
            }
        }
    }

    fun revertInOutLanguage() {
        val temp = _currentInputCountry.value
        _currentInputCountry.value = _currentOutputCountry.value
        _currentOutputCountry.value = temp
    }

}