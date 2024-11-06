package com.example.ocean.domain.usecase

import android.graphics.Bitmap
import com.example.ocean.domain.repository.ImageUtilRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class GetImageFromURLUseCase (
    private val imageUtilRepository: ImageUtilRepository,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : UseCase<String, Bitmap?>(coroutineDispatcher) {

    override suspend fun execute(parameters: String): Bitmap? {
        return imageUtilRepository.loadImageFromUrl(parameters)
    }

}