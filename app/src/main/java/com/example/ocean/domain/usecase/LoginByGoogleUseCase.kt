package com.example.ocean.domain.usecase

import com.example.ocean.domain.repository.GoogleAuthenticationRepository
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class LoginByGoogleUseCase(
    private val googleAuthenticationRepository: GoogleAuthenticationRepository,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<Unit, GoogleIdTokenCredential>(coroutineDispatcher) {
    override suspend fun execute(parameters: Unit) : GoogleIdTokenCredential {
        return googleAuthenticationRepository.signInWithGoogle()
    }

}