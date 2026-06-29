package com.alezandrow.simplecleanarchitecture.data.source.network

import android.content.Context
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import com.alezandrow.simplecleanarchitecture.data.mapper.toAuthUser
import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val credentialManager: CredentialManager
) {

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
        password: String,
    ): AuthUser {
        return auth
            .createUserWithEmailAndPassword(email, password)
            .await()
            .toAuthUser()
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

    suspend fun requestPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    suspend fun updatePassword(currentPassword: String, newPassword: String) {
        val user = auth.currentUser!!
        val credential = EmailAuthProvider.getCredential(
            user.email!!, currentPassword
        )
        user.reauthenticate(credential).await()
        auth.currentUser?.updatePassword(newPassword)?.await()
    }

    fun deleteUser() {
        auth.currentUser!!.delete()
    }

    suspend fun saveCredential(email: String, password: String, context: Context) {
        val request = CreatePasswordRequest(id = email, password = password)
        credentialManager.createCredential(
            context = context,
            request = request
        )
    }

    suspend fun getSavedCredential(context: Context): Credential {
        val passwordOption = GetPasswordOption(isAutoSelectAllowed = true)
        val getCredentialRequest = GetCredentialRequest(
            credentialOptions = listOf(passwordOption)
        )

        val result = credentialManager.getCredential(
            context = context,
            request = getCredentialRequest
        )

        return result.credential
    }

}