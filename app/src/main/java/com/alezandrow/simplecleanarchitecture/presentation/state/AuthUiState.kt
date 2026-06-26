package com.alezandrow.simplecleanarchitecture.presentation.state

sealed class AuthUiState {

    data object Idle : AuthUiState()

    data object Loading : AuthUiState()

    data class Error(
        val message: String
    ) : AuthUiState()

    data object Success : AuthUiState()
}