package com.example.ocean.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.ocean.R
import com.example.ocean.domain.repository.GoogleAuthenticationRepository
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GoogleAuthenticationRepositoryImpl @Inject constructor(
    private val credentialManager: CredentialManager,
    @ApplicationContext private val context: Context
) : GoogleAuthenticationRepository {

    override suspend fun signInWithGoogle(): GoogleIdTokenCredential {
        val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(
            context.getString(R.string.server_client_id)
        )
            .setNonce(nonce = null)
            .build()

        val requestWithSignInOptions = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        val result = credentialManager.getCredential(
            request = requestWithSignInOptions,
            context = context,
        )
        return GoogleIdTokenCredential.createFrom(result.credential.data)
    }
}