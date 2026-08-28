package com.alezandrow.simplecleanarchitecture.domain.entities.user

data class AuthUser(
    val uid: String,
    val email: String,
    val isEmailVerified: Boolean = false,
    val providerId: String = "",
    val photoUrl: String? = null,
    val displayName: String = ""
)