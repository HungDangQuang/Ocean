package com.example.ocean.domain.usecase

import androidx.datastore.preferences.core.Preferences
import com.example.ocean.domain.repository.DataStoreRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class GetCurrentCountryUseCase(
    private val key: Preferences.Key<String>,
    private val dataStoreRepository: DataStoreRepository,
    private val defaultCountryName: String,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
): UseCase<Unit, String>(coroutineDispatcher) {
    override suspend fun execute(parameters: Unit): String {
        return dataStoreRepository.getCurrentCountryName(key, defaultCountryName)
    }

}