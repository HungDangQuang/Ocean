package com.example.ocean.di

import android.content.Context
import com.example.ocean.data.repository.DataStoreRepositoryImpl
import com.example.ocean.data.repository.datasource.DataStorePreferences
import com.example.ocean.domain.usecase.GetImagesDownloadedFlagUseCase
import com.example.ocean.domain.usecase.StoreImagesDownloadedFlagUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDataStorePreferences(
        @ApplicationContext context: Context
    ) = DataStorePreferences(context)

    @Singleton
    @Provides
    fun provideDataStoreRepository(
        dataStorePreferences: DataStorePreferences
    ) = DataStoreRepositoryImpl(dataStorePreferences)

    @Singleton
    @Provides
    fun provideGetImagesDownloadedFlagUseCase(
        dataStoreRepository: DataStoreRepositoryImpl
    ) = GetImagesDownloadedFlagUseCase(dataStoreRepository)


    @Singleton
    @Provides
    fun provideStoreImagesDownloadedFlagUseCase(
        dataStoreRepository: DataStoreRepositoryImpl
    ) = StoreImagesDownloadedFlagUseCase(dataStoreRepository)


}