package com.alezandrow.simplecleanarchitecture.common

sealed interface AppError {

    data object Network : AppError

    data object InvalidCredentials : AppError

    data object UserNotFound : AppError

    data object PermissionDenied: AppError

    data object NotFound : AppError

    data object AlreadyExists : AppError

    data object Timeout : AppError

    data object OperationAborted : AppError

    data object Cancelled : AppError

    data object Unauthenticated : AppError

    data class Validation(
        val message: String
    ) : AppError

    data class Unknown(
        val message: String
    ) : AppError

    data object EmailAlreadyInUse : AppError
}