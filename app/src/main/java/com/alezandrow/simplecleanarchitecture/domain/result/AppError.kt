package com.alezandrow.simplecleanarchitecture.domain.result

sealed interface AppError {

    data object Network : AppError

    data object InvalidCredentials : AppError

    data object UserNotFound : AppError

    data class Validation(
        val message: String
    ) : AppError

    data class Unknown(
        val message: String
    ) : AppError

    data object EmailAlreadyInUse : AppError
}