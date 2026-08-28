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

class SignInUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var signInUseCase: SignInUseCase

    @Before
    fun setup() {
        authRepository = mockk<AuthRepository>(relaxed = true)
        signInUseCase = SignInUseCase(authRepository)
    }

    @Test
    fun `invoke should return success when repository signIn succeeds`() = runTest {

        val email = "test@example.com"
        val password = "password123"

        val authUser = AuthUser(
            uid = "xyz",
            email = "test@example.com",
            isEmailVerified = true,
            providerId = "password",
            photoUrl = "xyz",
            displayName = "test"
        )

        val expected = AppResult.Success(authUser)

        coEvery {
            authRepository.signIn(email, password)
        } returns expected

        val result = signInUseCase(email, password)

        assertEquals(expected, result)

        coVerify(exactly = 1) {
            authRepository.signIn(email, password)
        }
    }

    @Test
    fun `invoke should return error when repository signIn fails`() = runTest {
        val email = "test@gmail.com"
        val password = "wrong-password"

        val expected = AppResult.Error(AppError.InvalidCredentials)

        coEvery {
            authRepository.signIn(email, password)
        } returns expected

        val result = signInUseCase(email, password)

        assertEquals(expected, result)
    }
}