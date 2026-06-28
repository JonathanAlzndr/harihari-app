package com.alezandrow.simplecleanarchitecture.domain.repository

import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signIn(email: String, password: String): AppResult<AuthUser>

    suspend fun signUp(email: String, password: String): AppResult<AuthUser>

    suspend fun signOut(): AppResult<Unit>

    fun observeCurrentUser(): Flow<AuthUser?>

    suspend fun sendEmailVerification()

    suspend fun refreshCurrentUser(): AuthUser?

    suspend fun requestPasswordResetEmail(email: String)

    suspend fun updatePassword(currentPassword: String, newPassword: String): AppResult<String>
}