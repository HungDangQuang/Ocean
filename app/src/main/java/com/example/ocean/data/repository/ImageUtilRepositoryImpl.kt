package com.example.ocean.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.ocean.domain.repository.ImageUtilRepository
import javax.inject.Inject

class ImageUtilRepositoryImpl @Inject constructor() : ImageUtilRepository {

    override suspend fun loadImageFromUrl(url: String): Bitmap? {
        val inputStream = java.net.URL(url).openStream()
        return BitmapFactory.decodeStream(inputStream)
    }

}