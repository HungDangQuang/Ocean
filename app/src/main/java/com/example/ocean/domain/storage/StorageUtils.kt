package com.example.ocean.domain.storage

interface StorageUtils {
    fun storeFileInLocalStorage(byteArray: ByteArray, fileName: String)
}