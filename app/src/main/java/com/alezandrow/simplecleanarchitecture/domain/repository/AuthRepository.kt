package com.alezandrow.simplecleanarchitecture.domain.repository

import androidx.credentials.Credential
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signIn(email: String, password: String): AppResult<AuthUser>

    suspend fun signUp(email: String, password: String): AppResult<AuthUser>

    suspend fun signOut(): AppResult<Unit>

    fun observeCurrentUser(): Flow<AuthUser?>

    suspend fun sendEmailVerification(): AppResult<String>

    suspend fun refreshCurrentUser(): AuthUser?

    suspend fun requestPasswordResetEmail(email: String): AppResult<String>

    suspend fun updatePassword(currentPassword: String, newPassword: String): AppResult<String>

    suspend fun deleteAccount(): AppResult<String>

    suspend fun saveCredential(email: String, password: String, context: Any): AppResult<Unit>

    suspend fun getSavedCredential(context: Any): AppResult<Credential>

    suspend fun signInWithGoogle(context: Any): AppResult<AuthUser>
}