package com.alezandrow.simplecleanarchitecture.data.mapper

import com.alezandrow.simplecleanarchitecture.common.AppError
import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthProvider
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import java.io.IOException

fun AuthResult.toAuthUser(): AuthUser {
    val user = requireNotNull(this.user)
    return AuthUser(
        uid = user.uid,
        email = user.email.orEmpty()
    )
}

fun FirebaseUser.toAuthUser(): AuthUser {

    val actualProviderId = providerData
        .map { it.providerId }
        .firstOrNull { it != FirebaseAuthProvider.PROVIDER_ID }
        ?: providerId

    return AuthUser(
        uid = uid,
        email = email.orEmpty(),
        isEmailVerified = isEmailVerified,
        displayName = displayName.orEmpty(),
        providerId = actualProviderId,
        photoUrl = photoUrl
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