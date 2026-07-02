package com.alezandrow.simplecleanarchitecture.data.repository

import android.content.Context
import androidx.credentials.Credential
import com.alezandrow.simplecleanarchitecture.common.AppError
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.data.mapper.FirebaseAuthErrorMapper
import com.alezandrow.simplecleanarchitecture.data.source.network.FirebaseAuthDataSource
import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.alezandrow.simplecleanarchitecture.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: FirebaseAuthDataSource
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
        return authDataSource.observeCurrentUser()
    }

    override suspend fun sendEmailVerification() {
        authDataSource.sendEmailVerification()
    }

    override suspend fun refreshCurrentUser(): AuthUser? {
        return authDataSource.refreshCurrentUser()
    }

    override suspend fun requestPasswordResetEmail(email: String) {
        authDataSource.requestPasswordResetEmail(email)
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

    override suspend fun saveCredential(
        email: String,
        password: String,
        context: Any
    ): AppResult<Unit> {
        val androidContext =
            context as? Context ?: return AppResult.Error(AppError.Unknown("Context is not valid"))
        return try {
            authDataSource.saveCredential(email, password, androidContext)
            AppResult.Success(Unit)
        } catch (e: Exception) {
            AppResult.Error(FirebaseAuthErrorMapper.map(e))
        }
    }

    override suspend fun getSavedCredential(context: Any): AppResult<Credential> {
        val androidContext =
            context as? Context ?: return AppResult.Error(AppError.Unknown("Context is not valid"))
        return try {
            val credential = authDataSource.getSavedCredential(androidContext)
            AppResult.Success(credential)
        } catch (e: Exception) {
            AppResult.Error(FirebaseAuthErrorMapper.map(e))
        }
    }

    override suspend fun signInWithGoogle(context: Any): AppResult<AuthUser> {
        val androidContext = context as? Context ?: return AppResult.Error(AppError.Unknown("Context is not valid"))

        return try {
            val authUser = authDataSource.signInWithGoogle(androidContext)
            AppResult.Success(authUser)
        } catch (e: Exception) {
            AppResult.Error(FirebaseAuthErrorMapper.map(e))
        }
    }

}