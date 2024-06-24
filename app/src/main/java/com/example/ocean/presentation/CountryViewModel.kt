package com.example.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ocean.Utils.Utility
import com.example.ocean.data.repository.CountryRepositoryImpl
import com.example.ocean.data.service.RetrofitInstance
import com.example.ocean.domain.storage.StorageUtils
import com.example.ocean.domain.usecase.GetCountryUseCase
import com.example.ocean.domain.usecase.GetImagesDownloadedFlagUseCase
import com.example.ocean.domain.usecase.Result
import com.example.ocean.domain.usecase.StoreImagesDownloadedFlagUseCase
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

class CountryViewModel(
    private val storeImagesDownloadedFlagUseCase: StoreImagesDownloadedFlagUseCase,
    private val getImagesDownloadedFlagUseCase: GetImagesDownloadedFlagUseCase): ViewModel() {

    private val TAG = CountryViewModel::class.java.simpleName
    private val TIME_OUT_MILLIS = 30000L
    private val _isAllFlagsDownloaded = MutableLiveData<Boolean>()
    val isAllFlagsDownloaded: LiveData<Boolean> = _isAllFlagsDownloaded

    private val storageUtils = object : StorageUtils {
        override fun storeFileInLocalStorage(byteArray: ByteArray, fileName: String) {
            Utility.saveImageToDisk(byteArray, fileName)
        }
    }



    suspend fun handleDownloadingFlagImages() {
        val isCountryFlagsDownloaded = getImagesDownloadedFlagUseCase(Unit)
        if (isCountryFlagsDownloaded is Result.Success) {
            if (isCountryFlagsDownloaded.data) {
                _isAllFlagsDownloaded.postValue(true)
            } else {
                _isAllFlagsDownloaded.postValue(false)
            }
        }
    }

    private suspend fun updateDownloadingStatus(newStatus: Boolean) {
        storeImagesDownloadedFlagUseCase(newStatus)
        _isAllFlagsDownloaded.postValue(newStatus)
    }

    suspend fun downloadListOfCountryFlags() {
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
                _isAllFlagsDownloaded.postValue(false)
                Log.e(TAG, "Timeout: Not all country flags were downloaded in time")
            }
        }
    }
}