package com.alezandrow.simplecleanarchitecture.domain.validation.validator

import com.alezandrow.simplecleanarchitecture.domain.validation.ValidationResult
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidateEmailUseCaseTest {

    private lateinit var useCase: ValidateEmailUseCase

    @Before
    fun setup() {
        useCase = ValidateEmailUseCase()
    }

    @Test
    fun `invoke should return error when email is blank`() {
        val result = useCase("")
        assertEquals(ValidationResult.Error("Email can't be empty"), result)
    }

    @Test
    fun `invoke should return error when email does not contain at`() {
        val result = useCase("userexample.com")
        assertEquals(ValidationResult.Error("Email is not valid"), result)
    }

    @Test
    fun `invoke should return error when email domain is blank`() {
        val result = useCase("user@")
        assertEquals(ValidationResult.Error("Domain is not valid"), result)
    }


    @Test
    fun `invoke should return error when email contains more than one at`() {
        val result =
            useCase("user@example.com@test.com")
        assertEquals (ValidationResult.Error("Domain is not valid"), result)
    }

    @Test
    fun `invoke should return success when email is valid`() {
        val result = useCase("user@example.com")
        assertEquals (ValidationResult.Success, result)
    }
}