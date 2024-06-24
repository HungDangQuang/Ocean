package com.example.ocean.domain.usecase

import com.example.ocean.domain.repository.DataStoreRepository
import com.example.ocean.domain.storage.DataStoreKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class GetImagesDownloadedFlagUseCase(
    private val dataStoreRepository: DataStoreRepository,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<Unit, Boolean>(coroutineDispatcher) {

    override suspend fun execute(parameters: Unit): Boolean {
        return dataStoreRepository.isCountryFlagImagesDownloaded(
            DataStoreKey.KEY_IS_COUNTRY_FLAG_IMAGES_DOWNLOADED,
            false
        )
    }

}