package com.example.ocean.presentation

import android.util.Log
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

    private val TAG = LoginViewModel::class.java.simpleName

    fun loginWithGoogleEmail() {

        viewModelScope.launch {
            val result = loginByGoogleUseCase(Unit)
            if (result is Result.Success) {
                val resultBody = result.data
                // todo handle success case later
            } else {
                // todo handle failure case later
                Log.d(TAG, "loginWithGoogleEmail: failed to get result")
            }
        }
    }
}