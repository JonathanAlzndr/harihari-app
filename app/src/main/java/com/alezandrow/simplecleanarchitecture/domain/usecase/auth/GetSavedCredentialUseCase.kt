package com.alezandrow.simplecleanarchitecture.domain.usecase.auth

import androidx.credentials.Credential
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.repository.AuthRepository
import javax.inject.Inject

class GetSavedCredentialUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(context: Any): AppResult<Credential> =
        authRepository.getSavedCredential(context)
}