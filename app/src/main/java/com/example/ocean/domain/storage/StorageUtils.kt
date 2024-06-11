package com.example.ocean.domain.storage

interface StorageUtils {
    suspend fun storeFileInLocalStorage(byteArray: ByteArray)
}