package com.alezandrow.simplecleanarchitecture.presentation.helper

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import com.alezandrow.simplecleanarchitecture.BuildConfig
import com.alezandrow.simplecleanarchitecture.common.AppError
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.entities.auth.AuthCredential
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class CredentialAuthHelper(
    private val context: Context,
    private val credentialManager: CredentialManager
) {

     suspend fun getPasswordCredential(): AppResult<AuthCredential.Password> {
        return try {
            val request = GetCredentialRequest(
                credentialOptions = listOf(GetPasswordOption(isAutoSelectAllowed = true))
            )
            val credential =
                credentialManager.getCredential(context = context, request = request).credential

            if (credential is PasswordCredential) {
                AppResult.Success(AuthCredential.Password(credential.id, credential.password))
            } else {
                AppResult.Error(AppError.Unknown("Saved credential is not a password credential"))
            }
        } catch (e: Exception) {
            AppResult.Error(AppError.Unknown(e.message ?: "No saved credential found"))
        }
    }

     suspend fun getGoogleCredential(): AppResult<AuthCredential.Google> {
        return try {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest(credentialOptions = listOf(googleIdOption))
            val credential = credentialManager.getCredential(context, request).credential

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val idTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                AppResult.Success(AuthCredential.Google(idTokenCredential.idToken))
            } else {
                AppResult.Error(AppError.Unknown("Credential is not a valid Google ID token"))
            }
        } catch (e: Exception) {
            AppResult.Error(AppError.Unknown(e.message ?: "Google sign-in failed"))
        }
    }

     suspend fun savePasswordCredential(
        email: String,
        password: String
    ): AppResult<Unit> {
        return try {
            credentialManager.createCredential(context, CreatePasswordRequest(email, password))
            AppResult.Success(Unit)
        } catch(e: Exception) {
            AppResult.Error(AppError.Unknown(e.message ?: "Failed to save credential"))
        }
    }

     suspend fun clearCredentialState(): AppResult<Unit> {
        return try {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            AppResult.Success(Unit)
        } catch(e: Exception) {
            AppResult.Error(AppError.Unknown(e.message ?: "Failed to clear credential state"))
        }
    }
}