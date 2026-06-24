package com.alezandrow.simplecleanarchitecture.data.source.network

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    fun observeCurrentUser(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        auth.addAuthStateListener(listener)
        trySend(auth.currentUser).isSuccess
        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }
}