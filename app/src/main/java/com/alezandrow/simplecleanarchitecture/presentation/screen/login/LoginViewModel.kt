package com.alezandrow.simplecleanarchitecture.presentation.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.alezandrow.simplecleanarchitecture.domain.result.AppResult
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.SignInUseCase
import com.alezandrow.simplecleanarchitecture.domain.validation.ValidationResult
import com.alezandrow.simplecleanarchitecture.domain.validation.validator.ValidateEmailUseCase
import com.alezandrow.simplecleanarchitecture.domain.validation.validator.ValidatePasswordUseCase
import com.alezandrow.simplecleanarchitecture.presentation.state.AuthUiState
import com.alezandrow.simplecleanarchitecture.presentation.state.LoginFormState
import com.alezandrow.simplecleanarchitecture.presentation.util.mapAppErrorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {

    private var _authUiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val authUiState = _authUiState.asStateFlow()

    private var _loginFormState = MutableStateFlow(LoginFormState())
    val loginFormState = _loginFormState.asStateFlow()

    fun onEmailChanged(newValue: String) {
        _loginFormState.update { it.copy(email = newValue) }
    }

    fun onPasswordChanged(newValue: String) {
        _loginFormState.update { it.copy(password = newValue) }
    }

    fun signIn() {
        val currentForm = _loginFormState.value
        val emailResult = validateEmailUseCase(currentForm.email)
        val passwordResult = validatePasswordUseCase(currentForm.password)

        val hasError = listOf(emailResult, passwordResult).any { it is ValidationResult.Error }

        if(hasError) {
            _loginFormState.update {
                it.copy(
                    emailError = (emailResult as? ValidationResult.Error)?.message,
                    passwordError =  (passwordResult as? ValidationResult.Error)?.message
                )
            }
            return
        }

        _authUiState.update { AuthUiState.Loading }

        viewModelScope.launch {
            when(val result = signInUseCase(currentForm.email, currentForm.password)) {
                is AppResult.Error -> {
                    _authUiState.value = AuthUiState.Error(mapAppErrorToMessage(result.error))
                }
                is AppResult.Success<AuthUser> -> {
                    _authUiState.value = AuthUiState.Success(result.data)
                }
            }
        }
    }
}