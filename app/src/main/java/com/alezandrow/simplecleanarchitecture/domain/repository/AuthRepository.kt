package com.alezandrow.simplecleanarchitecture.domain.repository

import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.alezandrow.simplecleanarchitecture.domain.result.AppResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signIn(email: String, password: String): AppResult<AuthUser>

    suspend fun signUp(email: String, password: String): AppResult<AuthUser>

    suspend fun signOut(): AppResult<Unit>

    fun observeCurrentUser(): Flow<AuthUser?>

}