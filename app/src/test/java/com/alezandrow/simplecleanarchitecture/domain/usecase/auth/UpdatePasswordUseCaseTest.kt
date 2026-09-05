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

class UpdatePasswordUseCaseTest {


    private lateinit var authRepository: AuthRepository
    private lateinit var useCase: UpdatePasswordUseCase

    @Before
    fun setup() {
        authRepository = mockk()
        useCase = UpdatePasswordUseCase(authRepository)
    }

    @Test
    fun `invoke should return success when password update succeeds`() = runTest {
        val currentPassword = "oldPassword123"
        val newPassword = "newPassword123"
        val expected = AppResult.Success("Password updated successfully")

        coEvery { authRepository.updatePassword(currentPassword, newPassword) } returns expected

        val result = useCase(currentPassword, newPassword)

        assertEquals(expected, result)

        coVerify(exactly = 1) {
            authRepository.updatePassword(currentPassword, newPassword)
        }
    }

    @Test
    fun `invoke should return error when password update fails`() = runTest {

        val currentPassword = "oldPassword123"
        val newPassword = "newPassword123"
        val expected = AppResult.Error(AppError.Unknown("Failed to update password"))

        coEvery { authRepository.updatePassword(currentPassword, newPassword) } returns expected

        val result = useCase(currentPassword, newPassword)

        assertEquals(expected, result)

        coVerify(exactly = 1) {
            authRepository.updatePassword(currentPassword, newPassword)
        }
    }


}