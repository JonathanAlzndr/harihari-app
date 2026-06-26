package com.alezandrow.simplecleanarchitecture.data.source.network

import com.alezandrow.simplecleanarchitecture.data.mapper.toAuthUser
import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(private val auth: FirebaseAuth) {

    private val _currentUser = MutableStateFlow(auth.currentUser?.toAuthUser())
    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _currentUser.value = firebaseAuth.currentUser?.toAuthUser()
    }

    init {
        auth.addAuthStateListener(authListener)
    }

    suspend fun signIn(
        email: String,
        password: String
    ): AuthUser? {
        return auth.signInWithEmailAndPassword(email, password).await()?.toAuthUser()
    }

    suspend fun signUp(
        email: String,
        password: String
    ): AuthUser {
        return auth.createUserWithEmailAndPassword(email, password).await().toAuthUser()
    }

    fun signOut() {
        auth.signOut()
    }

    fun observeCurrentUser(): Flow<AuthUser?> =
        _currentUser.asStateFlow()

    suspend fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.await()
    }

    suspend fun refreshCurrentUser(): AuthUser? {
        auth.currentUser?.reload()?.await()
        val user = auth.currentUser?.toAuthUser()
        _currentUser.value = user
        return user
    }

}