package com.example.ocean.domain.usecase

import com.example.ocean.domain.repository.LocalStorageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class GetImageFromURLUseCase (
    private val localStorageRepository: LocalStorageRepository,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UseCase<String, String?>(coroutineDispatcher) {

    override suspend fun execute(parameters: String): String {
        return localStorageRepository.storeImageFromUri(parameters)
    }

}