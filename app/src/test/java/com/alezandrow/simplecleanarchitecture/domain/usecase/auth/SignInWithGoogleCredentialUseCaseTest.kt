package com.alezandrow.simplecleanarchitecture.domain.usecase.auth

import com.alezandrow.simplecleanarchitecture.common.AppError
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.entities.auth.AuthCredential
import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.alezandrow.simplecleanarchitecture.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SignInWithGoogleCredentialUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var useCase: SignInWithGoogleCredentialUseCase

    @Before
    fun setup() {
        authRepository = mockk()
        useCase = SignInWithGoogleCredentialUseCase(authRepository)
    }

    @Test
    fun `invoke should return success when sign in with google credential succeeds`() = runTest {
        val credential = mockk<AuthCredential.Google>()
        val user = mockk<AuthUser>()
        val expected = AppResult.Success(user)

        coEvery {
            authRepository.signInWithGoogleCredential(credential)
        } returns expected

        val result = useCase(credential)

        assertEquals(expected, result)

        coVerify(exactly = 1) {
            authRepository.signInWithGoogleCredential(credential)
        }
    }

    @Test
    fun `invoke should return error when sign in with google credential fails`() = runTest {
        val credential = mockk<AuthCredential.Google>()
        val expected = AppResult.Error(AppError.InvalidCredentials)

        coEvery {
            authRepository.signInWithGoogleCredential(credential)
        } returns expected

        val result = useCase(credential)

        assertEquals(expected, result)

        coVerify(exactly = 1) {
            authRepository.signInWithGoogleCredential(credential)
        }

    }
}