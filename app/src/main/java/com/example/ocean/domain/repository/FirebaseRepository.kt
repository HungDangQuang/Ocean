package com.example.ocean.domain.repository

import com.google.firebase.auth.FirebaseUser

interface FirebaseRepository {
    suspend fun loginByGoogleAccount(idToken: String): FirebaseUser
}