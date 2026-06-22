package com.alezandrow.simplecleanarchitecture.domain.validation.validator

import com.alezandrow.simplecleanarchitecture.domain.validation.ValidationResult
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {
    operator fun invoke(password: String): ValidationResult {

        if (password.isBlank()) {
            return ValidationResult.Error("Password can not be empty")
        }

        if (password.length < 8) {
            return ValidationResult.Error("Password is minimal 8 characters")
        }

        val hasLetter = password.any { it.isLetter() }
        val hasDigit = password.any { it.isDigit() }

        if (!hasLetter || !hasDigit) {
            return ValidationResult.Error("Password must contains alphabet and number")
        }

        return ValidationResult.Success
    }
}