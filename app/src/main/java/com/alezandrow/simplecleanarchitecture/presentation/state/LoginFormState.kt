package com.alezandrow.simplecleanarchitecture.presentation.state

data class LoginFormState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null
)