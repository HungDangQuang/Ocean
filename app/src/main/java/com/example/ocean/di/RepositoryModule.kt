package com.example.ocean.di

import com.example.ocean.data.repository.GoogleAuthenticationRepositoryImpl
import com.example.ocean.data.repository.ImageUtilRepositoryImpl
import com.example.ocean.domain.repository.GoogleAuthenticationRepository
import com.example.ocean.domain.repository.ImageUtilRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindGoogleAuthenticationRepository(
        googleAuthenticationRepositoryImpl: GoogleAuthenticationRepositoryImpl
    ): GoogleAuthenticationRepository

    @Binds
    @Singleton
    abstract fun bindImageUtilRepository(
        imageUtilRepositoryImpl: ImageUtilRepositoryImpl
    ): ImageUtilRepository

}