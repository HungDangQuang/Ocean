package com.example.ocean.domain.repository

import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

interface GoogleAuthenticationRepository {
    suspend fun signInWithGoogle(): GoogleIdTokenCredential
}