package com.alezandrow.simplecleanarchitecture.domain.usecase.auth

import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.repository.AuthRepository
import javax.inject.Inject

class RequestPasswordResetUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): AppResult<String> = authRepository.requestPasswordResetEmail(email)
}