package com.alezandrow.simplecleanarchitecture.data.source.network

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(private val auth: FirebaseAuth) {

    suspend fun signIn(
        email: String,
        password: String
    ): AuthResult {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signUp(
        email: String,
        password: String
    ): AuthResult {
        return auth.createUserWithEmailAndPassword(email, password).await()
    }

    fun signOut() {
        auth.signOut()
    }
}