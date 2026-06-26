package com.alezandrow.simplecleanarchitecture.domain.usecase.auth

import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.alezandrow.simplecleanarchitecture.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<AuthUser?> = authRepository.observeCurrentUser()
}