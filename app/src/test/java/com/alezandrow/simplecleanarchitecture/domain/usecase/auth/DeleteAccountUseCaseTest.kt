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

class DeleteAccountUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var useCase: DeleteAccountUseCase

    @Before
    fun setup() {
        authRepository = mockk()
        useCase = DeleteAccountUseCase(authRepository)
    }

    @Test
    fun `invoke should return success when delete account succeeds`() = runTest {

        val expected = AppResult.Success("Account deleted")

        coEvery { authRepository.deleteAccount() } returns expected

        val result = useCase()
        assertEquals(expected, result)
        coVerify(exactly = 1) {
            authRepository.deleteAccount()
        }
    }

    @Test
    fun `invoke should return error when delete account fails`() = runTest {

        val expected = AppResult.Error(
            AppError.UserNotFound
        )

        coEvery { authRepository.deleteAccount() } returns expected

        val result = useCase()

        assertEquals(expected, result)

        coVerify(exactly = 1) { authRepository.deleteAccount() }
    }

}