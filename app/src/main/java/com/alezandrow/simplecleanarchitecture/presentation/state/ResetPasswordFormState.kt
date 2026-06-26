package com.alezandrow.simplecleanarchitecture.presentation.state

data class ResetPasswordFormState(
    val email: String = "",
    val emailError: String? = null,
)