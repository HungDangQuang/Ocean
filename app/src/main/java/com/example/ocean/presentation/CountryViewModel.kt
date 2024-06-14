package com.example.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Country
import com.example.ocean.Utils.Utility
import com.example.ocean.data.CountryRepositoryImpl
import com.example.ocean.data.RetrofitInstance
import com.example.ocean.domain.storage.StorageUtils
import com.example.ocean.domain.usecase.GetCountryUseCase
import com.example.ocean.domain.usecase.Result
import com.example.ocean.ui.component.plash.StartupFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class CountryViewModel(private val getCountryUseCase: GetCountryUseCase): ViewModel() {

    private val TAG = CountryViewModel::class.java.simpleName
    private val TIME_OUT_MILLIS = 30000L
    private val _isAllFlagsDownloaded = MutableLiveData<Boolean>()
    private val storageUtils = object : StorageUtils {
        override fun storeFileInLocalStorage(byteArray: ByteArray, fileName: String) {
            Utility.saveImageToDisk(byteArray, fileName)
        }
    }

    init {
        downloadListOfCountryFlags()
    }



    private fun fetchListOfCountryFlags() = viewModelScope.launch {
//        val countryList = getCountryUseCase.execute()
//        _countries.value = countryList
        val result = getCountryUseCase(Unit)
        if (result is Result.Success) {
            Log.d(TAG, "fetchCountries: successful result")
        } else {
            Log.d(TAG, "fetchCountries: failed result")
        }

    }

    private fun updateDownloadingStatus(newStatus: Boolean) {
        _isAllFlagsDownloaded.postValue(newStatus)
    }

    fun getDownloadingStatus(): LiveData<Boolean> {
        return _isAllFlagsDownloaded
    }

    private fun downloadListOfCountryFlags() {
        val countryRepository = CountryRepositoryImpl(RetrofitInstance.apiService)
        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            val deferredResults = Utility.getListOfCountryCode().asFlow().map { countryCode ->
                async {
                    Log.d(TAG, "downloadListOfCountryFlag: $countryCode")
                    val getCountryUseCase =
                        GetCountryUseCase(countryRepository, countryCode, storageUtils)
                    getCountryUseCase(Unit)
                }
            }
                .buffer()
                .toList()

            try {
                // Await all deferred tasks within a specified time limit
                withTimeout(TIME_OUT_MILLIS) {
                    deferredResults.awaitAll()
                }
                updateDownloadingStatus(true)
                Log.d(TAG, "All country flags downloaded within the time limit")
            } catch (e: TimeoutCancellationException) {
                Log.e(TAG, "Timeout: Not all country flags were downloaded in time")
            }
        }
    }
}