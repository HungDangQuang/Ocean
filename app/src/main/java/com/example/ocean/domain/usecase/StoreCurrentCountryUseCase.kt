package com.example.ocean.domain.usecase

import androidx.datastore.preferences.core.Preferences
import com.example.ocean.domain.repository.DataStoreRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class StoreCurrentCountryUseCase(
    private val key: Preferences.Key<String>,
    private val dataStoreRepository: DataStoreRepository,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<String, Unit>(coroutineDispatcher){

    override suspend fun execute(parameters: String) {
        dataStoreRepository.setCurrentCountryName(key, parameters)
    }
}