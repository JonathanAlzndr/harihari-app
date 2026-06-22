package com.alezandrow.simplecleanarchitecture.data.mapper

import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.alezandrow.simplecleanarchitecture.domain.result.AppError
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.tasks.await
import java.io.IOException

suspend fun AuthResult.toAuthUser(getIdToken: Boolean = false): AuthUser {
    val user = requireNotNull(this.user)
    val token = if(getIdToken) {
        user.getIdToken(true).await().token.orEmpty()
    } else {
        ""
    }
    return AuthUser(
        uid = user.uid,
        email = user.email.orEmpty(),
        token = token
    )
}

object FirebaseAuthErrorMapper {

    fun map(e: Exception): AppError {
        return when (e) {

            is FirebaseAuthInvalidCredentialsException ->
                AppError.InvalidCredentials

            is FirebaseAuthInvalidUserException ->
                AppError.UserNotFound

            is FirebaseAuthUserCollisionException ->
                AppError.EmailAlreadyInUse

            is IOException ->
                AppError.Network

            else ->
                AppError.Unknown(e.message ?: "Unknown error")
        }
    }
}