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

class SendEmailVerificationUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var useCase: SendEmailVerificationUseCase


    @Before
    fun setup() {
        authRepository = mockk()
        useCase = SendEmailVerificationUseCase(authRepository)
    }

    @Test
    fun `invoke should return success when email verification is sent`() = runTest {
        val expected = AppResult.Success("Verification email sent")
        coEvery { authRepository.sendEmailVerification() } returns expected

        val result = useCase()

        assertEquals(expected, result)
        coVerify(exactly = 1) {
            authRepository.sendEmailVerification()
        }
    }

    @Test
    fun `invoke should return error when sending email verification fails`() = runTest {
        val expected = AppResult.Error(AppError.Network)

        coEvery { authRepository.sendEmailVerification() } returns expected

        val result = useCase()

        assertEquals(expected, result)

        coVerify(exactly = 1) {
            authRepository.sendEmailVerification()
        }
    }

}