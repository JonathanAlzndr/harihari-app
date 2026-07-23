package com.alezandrow.simplecleanarchitecture.presentation.state

sealed class AuthEvent {
    data class ShowSnackbar(val message: String): AuthEvent()
}