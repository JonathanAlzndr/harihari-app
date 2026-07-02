package com.alezandrow.simplecleanarchitecture.domain.usecase.auth

import com.alezandrow.simplecleanarchitecture.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(context: Any) = authRepository.signInWithGoogle(context)
}