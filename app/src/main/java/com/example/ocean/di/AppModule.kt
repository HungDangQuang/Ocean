package com.example.ocean.di

import android.content.Context
import com.example.ocean.Utils.Constants
import com.example.ocean.data.repository.DataStoreRepositoryImpl
import com.example.ocean.data.repository.LocalStorageRepositoryImpl
import com.example.ocean.data.repository.TranslationRepositoryImpl
import com.example.ocean.data.repository.datasource.DataStorePreferences
import com.example.ocean.data.service.RetrofitInstance
import com.example.ocean.domain.repository.GoogleAuthenticationRepository
import com.example.ocean.domain.repository.ImageUtilRepository
import com.example.ocean.domain.repository.LocalStorageRepository
import com.example.ocean.domain.storage.DataStoreKey
import com.example.ocean.domain.usecase.GetCurrentCountryUseCase
import com.example.ocean.domain.usecase.GetImageFromURLUseCase
import com.example.ocean.domain.usecase.GetImagesDownloadedFlagUseCase
import com.example.ocean.domain.usecase.LoginByGoogleUseCase
import com.example.ocean.domain.usecase.StoreCurrentCountryUseCase
import com.example.ocean.domain.usecase.StoreImagesDownloadedFlagUseCase
import com.example.ocean.domain.usecase.TranslateTextUseCase
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

    @Provides
    @Singleton
    fun provideLocalStorageRepository() : LocalStorageRepository = LocalStorageRepositoryImpl()

    @Singleton
    @Provides
    @Named("currentInputCountry")
    fun provideCurrentInputCountryUseCase(
        dataStoreRepository: DataStoreRepositoryImpl
    ) = GetCurrentCountryUseCase(
        DataStoreKey.KEY_CURRENT_INPUT_COUNTRY_NAME,
        dataStoreRepository,
        Constants.DEFAULT_INPUT_COUNTRY
    )

    @Singleton
    @Provides
    @Named("currentOutputCountry")
    fun provideCurrentOutputCountryUseCase(
        dataStoreRepository: DataStoreRepositoryImpl
    ) = GetCurrentCountryUseCase(
        DataStoreKey.KEY_CURRENT_OUTPUT_COUNTRY_NAME,
        dataStoreRepository,
        Constants.DEFAULT_OUTPUT_COUNTRY
    )

    @Singleton
    @Provides
    @Named("storeCurrentInputCountry")
    fun provideStoreCurrentInputCountryUseCase(
        dataStoreRepository: DataStoreRepositoryImpl
    ) = StoreCurrentCountryUseCase(
        DataStoreKey.KEY_CURRENT_INPUT_COUNTRY_NAME,
        dataStoreRepository
    )

    @Singleton
    @Provides
    @Named("storeCurrentOutputCountry")
    fun provideStoreCurrentOutputCountryUseCase(
        dataStoreRepository: DataStoreRepositoryImpl
    ) = StoreCurrentCountryUseCase(
        DataStoreKey.KEY_CURRENT_OUTPUT_COUNTRY_NAME,
        dataStoreRepository
    )

    @Singleton
    @Provides
    fun provideTranslationRepositoryImpl() = TranslationRepositoryImpl(RetrofitInstance.translationService)

    @Singleton
    @Provides
    fun provideTranslateTextUseCase(
        translationRepository: TranslationRepositoryImpl
    ) = TranslateTextUseCase(
        translationRepository
    )

    @Singleton
    @Provides
    fun provideLoginByGoogleUseCase(
        googleAuthenticationRepository: GoogleAuthenticationRepository
    ) = LoginByGoogleUseCase(
        googleAuthenticationRepository
    )

    @Singleton
    @Provides
    fun provideGetImageFromURLUseCase(
        localStorageRepository: LocalStorageRepository
    ) = GetImageFromURLUseCase(
        localStorageRepository
    )

}