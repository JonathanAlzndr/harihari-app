package com.alezandrow.simplecleanarchitecture.domain.usecase.auth

import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.alezandrow.simplecleanarchitecture.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class RefreshCurrentUserUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var useCase: RefreshCurrentUserUseCase

    @Before
    fun setup() {
        authRepository = mockk()
        useCase = RefreshCurrentUserUseCase(authRepository)
    }

    @Test
    fun `invoke should return current user`() = runTest {
        val user = AuthUser(
            uid = "1",
            email = "test@gmail.com",
            isEmailVerified = true,
            providerId = "password",
            photoUrl = "",
            displayName = "user"
        )

        coEvery {
            authRepository.refreshCurrentUser()
        } returns user

        val result = useCase()

        assertEquals(user, result)

        coVerify(exactly = 1) {
            authRepository.refreshCurrentUser()
        }

    }

    @Test
    fun `invoke should return null when current user is not available`() = runTest {

        coEvery { authRepository.refreshCurrentUser() } returns null

        val result = useCase()

        assertNull(result)

        coVerify(exactly = 1) {
            authRepository.refreshCurrentUser()
        }
    }
}