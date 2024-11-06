package com.example.ocean.presentation

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocean.domain.usecase.GetImageFromURLUseCase
import com.example.ocean.domain.usecase.LoginByGoogleUseCase
import com.example.ocean.domain.usecase.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginByGoogleUseCase: LoginByGoogleUseCase,
    private val getImageFromURLUseCase: GetImageFromURLUseCase
) : ViewModel() {

    private val TAG = LoginViewModel::class.java.simpleName

    private val _userGmail = MutableLiveData<String>()
    val userGmail: LiveData<String> = _userGmail

    private val _displayName = MutableLiveData<String>()
    val displayName: LiveData<String> = _displayName

    private val _avatarBitmap = MutableLiveData<Bitmap>()
    val avatarBitmap: LiveData<Bitmap> = _avatarBitmap

    fun loginWithGoogleEmail() {

        viewModelScope.launch {
            val result = loginByGoogleUseCase(Unit)
            if (result is Result.Success) {
                val resultBody = result.data
                Log.d(TAG, "loginWithGoogleEmail email: ${resultBody.id}")
                _userGmail.postValue(resultBody.id)
                Log.d(TAG, "loginWithGoogleEmail display name: ${resultBody.displayName}")
                _displayName.postValue(resultBody.displayName)
                Log.d(TAG, "loginWithGoogleEmail uri: ${resultBody.profilePictureUri}")
                loadImageFromURL(resultBody.profilePictureUri.toString())
            } else {
                // todo handle failure case later
                Log.d(TAG, "loginWithGoogleEmail: failed to get result")
            }
        }
    }

    private fun loadImageFromURL(url: String) {
        viewModelScope.launch {
            val result = getImageFromURLUseCase(url)
            if (result is Result.Success) {
                Log.d(TAG, "loadImageFromURL: successfully loaded")
                _avatarBitmap.postValue(result.data)
            } else {
                Log.d(TAG, "loadImageFromURL: Failed to load image bitmap")
            }
        }
    }
}