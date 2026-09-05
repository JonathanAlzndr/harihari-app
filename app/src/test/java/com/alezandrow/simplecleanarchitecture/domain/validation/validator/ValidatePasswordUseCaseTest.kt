package com.alezandrow.simplecleanarchitecture.domain.validation.validator

import com.alezandrow.simplecleanarchitecture.domain.validation.ValidationResult
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ValidatePasswordUseCaseTest {

    private lateinit var useCase: ValidatePasswordUseCase

    @Before
    fun setup() {
        useCase = ValidatePasswordUseCase()
    }

    @Test
    fun `invoke should return error when password is blank`() {
        val result = useCase("")
        assertEquals(ValidationResult.Error("Password can not be empty"), result)
    }

    @Test
    fun `invoke should return error when password is less than 8 characters`() {
        val result = useCase("abc123")
        assertEquals(ValidationResult.Error("Password is minimal 8 characters"), result)
    }

    @Test
    fun `invoke should return error when password contains no letter`() {
        val result =
            useCase("12345678")
        assertEquals (ValidationResult.Error("Password must contains alphabet and number"), result)
    }

    @Test
    fun `invoke should return error when password contains no digit`() {
        val result =
            useCase("abcdefgh")
        assertEquals (ValidationResult.Error("Password must contains alphabet and number"), result)
    }

    @Test
    fun `invoke should return success when password contains letter and digit`() {
        val result = useCase("password123")
        assertEquals (ValidationResult.Success, result)
    }

    @Test
    fun `invoke should return success when password has exactly 8 characters`() {
        val result = useCase("pass1234")
        assertEquals (ValidationResult.Success, result)
    }
}