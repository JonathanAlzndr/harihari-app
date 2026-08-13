package com.alezandrow.simplecleanarchitecture.presentation.util

import com.alezandrow.simplecleanarchitecture.common.AppError

fun mapAppErrorToMessage(error: AppError): String {
    return when (error) {
        is AppError.Network -> "Connection issue"
        is AppError.InvalidCredentials -> "Email or password wrong."
        is AppError.UserNotFound -> "Account is not found"
        is AppError.EmailAlreadyInUse -> "Email is already in use."
        is AppError.AlreadyExists -> "Document Already Exists"
        is AppError.Cancelled -> "Process Cancelled"
        is AppError.PermissionDenied -> "Permission Denied"
        is AppError.Validation -> error.message
        is AppError.Unknown -> error.message
        else -> { "Unknown Error" }
    }
}