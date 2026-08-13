package com.alezandrow.simplecleanarchitecture.domain.entities.user

import android.net.Uri

data class AuthUser(
    val uid: String,
    val email: String,
    val isEmailVerified: Boolean = false,
    val providerId: String = "",
    val photoUrl: Uri? = null,
    val displayName: String = ""
)