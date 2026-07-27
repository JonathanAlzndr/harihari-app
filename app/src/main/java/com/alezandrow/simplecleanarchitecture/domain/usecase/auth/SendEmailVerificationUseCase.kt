package com.alezandrow.simplecleanarchitecture.domain.usecase.auth

import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.repository.AuthRepository
import javax.inject.Inject

class SendEmailVerificationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): AppResult<String> = authRepository.sendEmailVerification()
}