package com.alezandrow.simplecleanarchitecture.presentation.screen.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.SaveCredentialUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.SendEmailVerificationUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.SignUpUseCase
import com.alezandrow.simplecleanarchitecture.domain.validation.ValidationResult
import com.alezandrow.simplecleanarchitecture.domain.validation.validator.ValidateEmailUseCase
import com.alezandrow.simplecleanarchitecture.domain.validation.validator.ValidatePasswordUseCase
import com.alezandrow.simplecleanarchitecture.presentation.state.AuthUiState
import com.alezandrow.simplecleanarchitecture.presentation.state.RegisterFormState
import com.alezandrow.simplecleanarchitecture.presentation.util.mapAppErrorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    private val saveCredentialUseCase: SaveCredentialUseCase,
) : ViewModel() {

    private var _authUiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val authUiState = _authUiState.asStateFlow()

    private val _formState = MutableStateFlow(RegisterFormState())
    val formState = _formState.asStateFlow()

    fun onEmailChanged(newValue: String) {
        _formState.update { it.copy(email = newValue) }
    }

    fun onPasswordChanged(newValue: String) {
        _formState.update { it.copy(password = newValue, passwordError = null) }
    }

    fun onConfirmPasswordChanged(newValue: String) {
        _formState.update { it.copy(confirmPassword = newValue, passwordError = null) }
    }

    fun signUp(context: Any) {
        val currentForm = _formState.value

        val emailResult = validateEmailUseCase(currentForm.email)
        val passwordResult = validatePasswordUseCase(currentForm.password)
        val hasPasswordMatch = currentForm.password == currentForm.confirmPassword

        val hasError = listOf(
            emailResult,
            passwordResult,
            hasPasswordMatch
        ).any { it is ValidationResult.Error } || !hasPasswordMatch

        if (hasError) {
            _formState.update {
                it.copy(
                    emailError = (emailResult as? ValidationResult.Error)?.message,
                    passwordError = (passwordResult as? ValidationResult.Error)?.message,
                    confirmPasswordError = if (!hasPasswordMatch) "Password is not matching" else null
                )
            }
            return
        }

        _authUiState.update { AuthUiState.Loading }

        viewModelScope.launch {
            when (val result = signUpUseCase(currentForm.email, currentForm.password)) {
                is AppResult.Error -> {
                    _authUiState.value = AuthUiState.Error(mapAppErrorToMessage(result.error))
                }

                is AppResult.Success<AuthUser> -> {
                    sendEmailVerificationUseCase()
                    saveCredential(currentForm.email, currentForm.password, context)
                    _authUiState.value = AuthUiState.Success
                }
            }
        }
    }

    private suspend fun saveCredential(email: String, password: String, context: Any) {
            when (val result = saveCredentialUseCase(email, password, context)) {
                is AppResult.Error -> {
                    Log.e("RegisterViewModel", "saveCredential: ${result.error}")
                }

                is AppResult.Success<*> -> {
                    Log.d("RegisterViewModel", "saveCredential: ${result.data}")
                }
            }
    }

}