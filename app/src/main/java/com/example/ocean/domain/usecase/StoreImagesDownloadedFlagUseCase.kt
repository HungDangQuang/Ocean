package com.example.ocean.domain.usecase


import com.example.ocean.domain.repository.DataStoreRepository
import com.example.ocean.domain.storage.DataStoreKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class StoreImagesDownloadedFlagUseCase(
    private val dataStoreRepository: DataStoreRepository,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<Boolean, Unit>(coroutineDispatcher) {

    override suspend fun execute(parameters: Boolean) {
        dataStoreRepository.setIsCountryFlagImagesDownloaded(
            DataStoreKey.KEY_IS_COUNTRY_FLAG_IMAGES_DOWNLOADED,
            parameters
        )
    }

}