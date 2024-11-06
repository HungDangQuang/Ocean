package com.example.ocean.domain.repository

import android.graphics.Bitmap

interface ImageUtilRepository {
    suspend fun loadImageFromUrl(url: String): Bitmap?
}