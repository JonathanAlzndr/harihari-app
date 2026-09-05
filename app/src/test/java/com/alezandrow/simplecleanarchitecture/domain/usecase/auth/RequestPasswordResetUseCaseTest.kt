package com.alezandrow.simplecleanarchitecture.domain.usecase.auth

import com.alezandrow.simplecleanarchitecture.common.AppError
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RequestPasswordResetUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var useCase: RequestPasswordResetUseCase

    @Before
    fun setup() {
        authRepository = mockk()
        useCase = RequestPasswordResetUseCase(authRepository)
    }

    @Test
    fun `invoke should return success when password reset request succeeds`() = runTest {
        val email = "user@example.com"
        val expected = AppResult.Success("Password reset email sent")

        coEvery { authRepository.requestPasswordResetEmail(email) } returns expected

        val result = useCase(email)
        assertEquals(expected, result)
        coVerify(exactly = 1) {
            authRepository.requestPasswordResetEmail(email)
        }
    }

    @Test
    fun `invoke should return error when password reset request fails`() = runTest {

        val email = "user@example.com"
        val expected = AppResult.Error(AppError.OperationAborted)

        coEvery {
            authRepository.requestPasswordResetEmail(
                email
            )
        } returns expected

        val result = useCase(email)
        assertEquals(expected, result)

        coVerify(exactly = 1) {
            authRepository.requestPasswordResetEmail(
                email
            )
        }
    }
}