package com.example.ocean.domain.usecase

import com.example.ocean.domain.repository.FirebaseRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class LoginByGoogleUseCase(
    private val firebaseRepository: FirebaseRepository,
    coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UseCase<String, Unit>(coroutineDispatcher) {

    override suspend fun execute(parameters: String) {
        firebaseRepository.loginByGoogleAccount(parameters)
    }

}