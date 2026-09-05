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

class SignOutUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var useCase: SignOutUseCase

    @Before
    fun setup() {
        authRepository = mockk()
        useCase = SignOutUseCase(authRepository)
    }

    @Test
    fun `invoke should return success when sign out succeeds`() = runTest {
        val expected = AppResult.Success(Unit)

        coEvery {
            authRepository.signOut()
        } returns expected

        val result = useCase()

        assertEquals(expected, result)

        coVerify(exactly = 1) {
            authRepository.signOut()
        }
    }

    @Test
    fun `invoke should return error when sign out fails`() = runTest {

        val expected =
            AppResult.Error(AppError.Unknown("Failed to sign out"))

        coEvery {
            authRepository.signOut()
        } returns expected

        val result = useCase()

        assertEquals (expected, result)

        coVerify(exactly = 1) {
            authRepository.signOut()
        }
    }
}