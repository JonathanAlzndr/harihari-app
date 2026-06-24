package com.alezandrow.simplecleanarchitecture.data.repository

import com.alezandrow.simplecleanarchitecture.data.mapper.FirebaseAuthErrorMapper
import com.alezandrow.simplecleanarchitecture.data.mapper.toAuthUser
import com.alezandrow.simplecleanarchitecture.data.source.network.FirebaseAuthDataSource
import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.alezandrow.simplecleanarchitecture.domain.repository.AuthRepository
import com.alezandrow.simplecleanarchitecture.domain.result.AppResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: FirebaseAuthDataSource
) : AuthRepository {

    override suspend fun signIn(
        email: String,
        password: String
    ): AppResult<AuthUser> {
        return try {
            val authResult = authDataSource.signIn(email, password)
            AppResult.Success(authResult.toAuthUser())
        } catch (e: Exception) {
            AppResult.Error(FirebaseAuthErrorMapper.map(e))
        }
    }

    override suspend fun signUp(
        email: String,
        password: String
    ): AppResult<AuthUser> {
        return try {
            val authResult = authDataSource.signUp(email, password)
            AppResult.Success(authResult.toAuthUser())
        } catch (e: Exception) {
            AppResult.Error(FirebaseAuthErrorMapper.map(e))
        }
    }

    override suspend fun signOut(): AppResult<Unit> {
        return try {
            authDataSource.signOut()
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(FirebaseAuthErrorMapper.map(e))
        }
    }

    override fun observeCurrentUser(): Flow<AuthUser?> {
        return authDataSource.observeCurrentUser()
            .map { firebaseUser ->
                firebaseUser?.toAuthUser()
            }
            .distinctUntilChanged()
    }

}