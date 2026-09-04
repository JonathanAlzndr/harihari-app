package com.alezandrow.simplecleanarchitecture.domain.entities.auth

sealed class AuthCredential {
    data class Password(val email: String, val password: String) : AuthCredential()
    data class Google(val idToken: String): AuthCredential()
}