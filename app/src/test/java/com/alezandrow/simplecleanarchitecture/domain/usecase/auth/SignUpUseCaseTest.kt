package com.alezandrow.simplecleanarchitecture.domain.usecase.auth

import com.alezandrow.simplecleanarchitecture.common.AppError
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.alezandrow.simplecleanarchitecture.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SignUpUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var useCase: SignUpUseCase

    @Before
    fun setup() { authRepository = mockk()
        useCase = SignUpUseCase(authRepository)
    }

    @Test
    fun `invoke should return success when sign up succeeds`() = runTest {
        val email = "user@example.com"
        val password = "password123"
        val user = mockk<AuthUser>()
        val expected = AppResult.Success(user)

        coEvery {
            authRepository.signUp(email, password)
        } returns expected

        val result = useCase(email, password)

        assertEquals(expected, result)

        coVerify(exactly = 1) {
            authRepository.signUp(email, password)
        }
    }

    @Test fun `invoke should return error when sign up fails`() = runTest {
        val email = "user@example.com"
        val password = "password123"
        val expected = AppResult.Error( AppError.Unknown("Failed to sign up") )

        coEvery {
            authRepository.signUp(email, password)
        } returns expected

        val result = useCase(email, password)

        assertEquals(expected, result)

        coVerify(exactly = 1) {
            authRepository.signUp(email, password) }
    }
}