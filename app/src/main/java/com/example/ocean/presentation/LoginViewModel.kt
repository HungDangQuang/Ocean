package com.example.ocean.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocean.domain.usecase.LoginByGoogleUseCase
import com.example.ocean.domain.usecase.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginByGoogleUseCase: LoginByGoogleUseCase
) : ViewModel() {

    fun loginWithGoogleEmail(tokenId: String) {
        viewModelScope.launch {
            val res = loginByGoogleUseCase(tokenId)
            if (res is Result.Success) {
                // Handle success case
                // TODO customize later
                println("LOGIN SUCCESS")
            } else {
                // TODO customize later
                println("LOGIN FAILED")
                // show notification login failed

            }
        }
    }
}