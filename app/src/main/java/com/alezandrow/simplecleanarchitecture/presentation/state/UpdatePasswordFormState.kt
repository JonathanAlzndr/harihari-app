package com.alezandrow.simplecleanarchitecture.presentation.state

data class UpdatePasswordFormState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val newPasswordError: String? = null,
    val confirmNewPassword: String = "",
    val confirmNewPasswordError: String? = null
)