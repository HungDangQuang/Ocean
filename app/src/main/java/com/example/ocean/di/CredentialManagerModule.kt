package com.example.ocean.di

import android.content.Context
import androidx.credentials.CredentialManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CredentialManagerModule {

    @Singleton
    @Provides
    fun provideCredentialManager(
        @ApplicationContext context: Context
    ): CredentialManager {
        return CredentialManager.create(context)
    }
}