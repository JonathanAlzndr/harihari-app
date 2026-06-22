package com.alezandrow.simplecleanarchitecture.domain.validation.validator

import com.alezandrow.simplecleanarchitecture.domain.validation.ValidationResult
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor() {
    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult.Error("Email can't be empty")
        }
        if (!email.contains("@")) {
            return ValidationResult.Error("Email is not valid")
        }
        val parts = email.split("@")
        if (parts.size != 2 || parts[1].isBlank()) {
            return ValidationResult.Error("Domain is not valid")
        }
        return ValidationResult.Success
    }
}