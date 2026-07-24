package com.alezandrow.simplecleanarchitecture.presentation.state

sealed class ProfileUiState<out T> {
    data class Success<T>(
        val userData: T
    ) : ProfileUiState<T>()

    data object Loading : ProfileUiState<Nothing>()

    data class Error(
        val message: String
    ) : ProfileUiState<Nothing>()
}