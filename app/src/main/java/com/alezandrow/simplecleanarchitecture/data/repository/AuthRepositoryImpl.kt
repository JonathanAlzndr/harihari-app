package com.alezandrow.simplecleanarchitecture.data.repository

import com.alezandrow.simplecleanarchitecture.common.AppError
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.data.mapper.FirebaseAuthErrorMapper
import com.alezandrow.simplecleanarchitecture.data.source.network.FirebaseAuthDataSource
import com.alezandrow.simplecleanarchitecture.data.source.network.SessionDataSource
import com.alezandrow.simplecleanarchitecture.domain.entities.auth.AuthCredential
import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.alezandrow.simplecleanarchitecture.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: FirebaseAuthDataSource,
    private val sessionDataSource: SessionDataSource
) : AuthRepository {

    override suspend fun signIn(
        email: String,
        password: String
    ): AppResult<AuthUser> {
        return try {
            val authUser = authDataSource.signIn(email, password)
            if (authUser != null) AppResult.Success(authUser)
            else AppResult.Error(AppError.UserNotFound)
        } catch (e: Exception) {
            AppResult.Error(FirebaseAuthErrorMapper.map(e))
        }
    }

    override suspend fun signUp(
        email: String,
        password: String
    ): AppResult<AuthUser> {
        return try {
            val authUser = authDataSource.signUp(email, password)
            AppResult.Success(authUser)
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
        return sessionDataSource.observeCurrentUser()
    }

    override suspend fun sendEmailVerification(): AppResult<String> {
        return try {
            authDataSource.sendEmailVerification()
            AppResult.Success("Email Sent")
        } catch (e: Exception) {
            AppResult.Error(FirebaseAuthErrorMapper.map(e))
        }
    }

    override suspend fun refreshCurrentUser(): AuthUser? {
        return sessionDataSource.refreshCurrentUser()
    }

    override suspend fun requestPasswordResetEmail(email: String): AppResult<String> {
        return try {
            authDataSource.requestPasswordResetEmail(email)
            AppResult.Success("Email Sent")
        } catch(e: Exception) {
            AppResult.Error(FirebaseAuthErrorMapper.map(e))
        }
    }

    override suspend fun updatePassword(
        currentPassword: String,
        newPassword: String
    ): AppResult<String> {
        return try {
            authDataSource.updatePassword(currentPassword, newPassword)
            AppResult.Success("Password Updated Successfully")
        } catch (e: Exception) {
            AppResult.Error(FirebaseAuthErrorMapper.map(e))
        }
    }

    override suspend fun deleteAccount(): AppResult<String> {
        return try {
            authDataSource.deleteUser()
            AppResult.Success("Account Deleted")
        } catch (e: Exception) {
            AppResult.Error(FirebaseAuthErrorMapper.map(e))
        }
    }

    override suspend fun signInWithGoogleCredential(credential: AuthCredential.Google): AppResult<AuthUser> {
        return try {
            val authUser = authDataSource.signInWithGoogleIdToken(credential.idToken)
            AppResult.Success(authUser)
        } catch(e: Exception) {
            AppResult.Error(FirebaseAuthErrorMapper.map(e))
        }
    }

}