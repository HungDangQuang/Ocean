package com.example.ocean.ui.component.login

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.ocean.databinding.ActivityLoginBinding
import com.example.ocean.presentation.LoginViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    private lateinit var oneTapClient: SignInClient
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        // Initialize One Tap client
        oneTapClient = Identity.getSignInClient(this)
        // Trigger One Tap Sign-In
        startOneTapSignIn()
    }

    private fun startOneTapSignIn() {
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId("251418567267-c14gfekqiedspb0n02dm4ss5u4pf1rmj.apps.googleusercontent.com")
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    oneTapSignInLauncher.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
                } catch (e: Exception) {
                    Log.e(ContentValues.TAG, "Error launching One Tap sign-in", e)
                }
            }
            .addOnFailureListener(this) { e ->
                Log.d(ContentValues.TAG, "One Tap sign-in failed")
                Log.d(ContentValues.TAG, "error: $e")
            }
    }

    private val oneTapSignInLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    loginViewModel.loginWithGoogleEmail(idToken)
                } else {
                    Log.e(ContentValues.TAG, "No ID token found!")
                }
            } catch (e: ApiException) {
                Log.e(ContentValues.TAG, "One Tap sign-in failed", e)
            }
        }
    }

}