package com.alezandrow.simplecleanarchitecture.domain.usecase.auth

import app.cash.turbine.test
import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.alezandrow.simplecleanarchitecture.domain.repository.AuthRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ObserveCurrentUserUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var useCase: ObserveCurrentUserUseCase

    @Before
    fun setup() {
        authRepository = mockk()
        useCase = ObserveCurrentUserUseCase(authRepository)
    }

    @Test
    fun `invoke should return current user flow`() = runTest {
        val user =
            AuthUser(
                uid = "1",
                email = "test@gmail.com",
                isEmailVerified = true,
                providerId = "password",
                photoUrl = "",
                displayName = "user"
            )

        every { authRepository.observeCurrentUser() } returns flowOf(user)

        useCase().test {
            assertEquals(user, awaitItem())
            awaitComplete()
        }

        verify(exactly = 1) {
            authRepository.observeCurrentUser()
        }
    }

    @Test
    fun `invoke should emit null when current user is not available`() = runTest {

        every { authRepository.observeCurrentUser() } returns flowOf(null)

        useCase().test {
            assertNull(awaitItem())
            awaitComplete()
        }

        verify(exactly = 1) {
            authRepository.observeCurrentUser()
        }
    }

    @Test
    fun `invoke should emit multiple users`() = runTest {

        val user1 =
            AuthUser(
                uid = "1",
                email = "test@gmail.com",
                isEmailVerified = true,
                providerId = "password",
                photoUrl = "",
                displayName = "user"
            )

        val user2 =
            AuthUser(
                uid = "2",
                email = "test2@gmail.com",
                isEmailVerified = true,
                providerId = "password",
                photoUrl = "",
                displayName = "user2"
            )

        every {
            authRepository.observeCurrentUser()
        } returns flowOf(user1, user2)


        useCase().test {
            assertEquals(user1, awaitItem())
            assertEquals(user2, awaitItem())
            awaitComplete()
        }

        verify(exactly = 1) {
            authRepository.observeCurrentUser()
        }
    }
}