package com.alezandrow.simplecleanarchitecture.presentation.state

import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser

sealed class AuthUiState {

    data object Idle : AuthUiState()

    data object Loading : AuthUiState()

    data class Success(
        val user: AuthUser
    ) : AuthUiState()

    data class Error(
        val message: String
    ) : AuthUiState()
}