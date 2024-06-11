package com.example.ocean.domain.usecase

import android.util.Log
import com.example.domain.model.Country
import com.example.domain.repository.CountryRepository
import com.example.domain.usecase.GetCountriesUseCase
import com.example.ocean.domain.storage.StorageUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

import okhttp3.ResponseBody
import retrofit2.Call

class GetCountryUseCase(
    private val countryRepository: CountryRepository,
    private val countryCode: String,
    private val storageUtils: StorageUtils,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<Unit, Unit>(coroutineDispatcher) {

    private val TAG = GetCountriesUseCase::class.java.simpleName

    override suspend fun execute(parameters: Unit): Unit {
       countryRepository.getCountry(countryCode).let { response ->
           if (response.isSuccessful) {
               Log.d(TAG, "image downloaded successfully")
               try {
                   val imageData = response.body()?.bytes() ?: throw Exception("Error")
                   Log.d(TAG, "response body: " + response.body())
                   Log.d(TAG, "image data size: " + imageData.size)
                   storageUtils.storeFileInLocalStorage(imageData)
               } catch (e: Exception) {
                   throw e
               }
           } else {
               Log.d(TAG, "failed to download image")
           }
       }

    }


}