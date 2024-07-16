package com.example.ocean.domain.usecase

import android.util.Log
import com.example.domain.repository.CountryRepository
import com.example.ocean.domain.storage.StorageUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class GetCountryUseCase(
    private val countryRepository: CountryRepository,
    private val countryCode: String,
    private val countryLanguage: String,
    private val storageUtils: StorageUtils,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<Unit, Unit>(coroutineDispatcher) {

    private val TAG = GetCountryUseCase::class.java.simpleName

    override suspend fun execute(parameters: Unit): Unit {
       countryRepository.getCountry(countryCode).let { response ->
           if (response.isSuccessful) {
               Log.d(TAG, "image $countryCode downloaded successfully")
               try {
                   val imageData = response.body()?.bytes() ?: throw Exception("Error")
                   storageUtils.storeFileInLocalStorage(imageData, countryLanguage)
               } catch (e: Exception) {
                   throw e
               }
           } else {
               Log.e(TAG, "failed to download image $countryCode")
           }
       }

    }


}