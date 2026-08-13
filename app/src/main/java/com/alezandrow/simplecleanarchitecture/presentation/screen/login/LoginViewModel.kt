package com.alezandrow.simplecleanarchitecture.presentation.screen.login

import android.content.Context
import android.util.Log
import androidx.credentials.PasswordCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.GetSavedCredentialUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.RefreshCurrentUserUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.SignInUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.SignInWithGoogleUseCase
import com.alezandrow.simplecleanarchitecture.domain.validation.ValidationResult
import com.alezandrow.simplecleanarchitecture.domain.validation.validator.ValidateEmailUseCase
import com.alezandrow.simplecleanarchitecture.domain.validation.validator.ValidatePasswordUseCase
import com.alezandrow.simplecleanarchitecture.presentation.state.AuthUiState
import com.alezandrow.simplecleanarchitecture.presentation.state.LoginFormState
import com.alezandrow.simplecleanarchitecture.util.mapAppErrorToMessage
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
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val refreshCurrentUserUseCase: RefreshCurrentUserUseCase,
    private val getSavedCredentialUseCase: GetSavedCredentialUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
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

    fun signInManual() {
        val currentForm = _loginFormState.value
        val emailResult = validateEmailUseCase(currentForm.email)
        val passwordResult = validatePasswordUseCase(currentForm.password)

        val hasError = listOf(emailResult, passwordResult).any { it is ValidationResult.Error }

        if (hasError) {
            _loginFormState.update {
                it.copy(
                    emailError = (emailResult as? ValidationResult.Error)?.message,
                    passwordError = (passwordResult as? ValidationResult.Error)?.message
                )
            }
            return
        }

        _authUiState.update { AuthUiState.Loading }

        viewModelScope.launch {
            executeLogin(currentForm.email, currentForm.password)
        }
    }

    fun signInWithAutoFill(context: Context) {
        viewModelScope.launch {
            when (val credentialResult = getSavedCredentialUseCase(context)) {
                is AppResult.Error -> {}
                is AppResult.Success -> {
                    val credential = credentialResult.data
                    if (credential is PasswordCredential) {
                        _authUiState.update { AuthUiState.Loading }
                        executeLogin(credential.id, credential.password)
                    }
                }
            }
        }
    }

    fun signInWithGoogle(context: Context) {
        _authUiState.update { AuthUiState.Loading }

        viewModelScope.launch {
            when(val result = signInWithGoogleUseCase(context)) {
                is AppResult.Error -> {
                    _authUiState.value = AuthUiState.Error(mapAppErrorToMessage(result.error))
                }
                is AppResult.Success -> {
                    val refreshedUser = refreshCurrentUserUseCase()
                    if(refreshedUser?.isEmailVerified == true) {
                        _authUiState.value = AuthUiState.Success
                    } else {
                        Log.d("LoginViewModel", "signInWithGoogle: $result")
                    }
                }
            }
        }
    }

    private suspend fun executeLogin(email: String, password: String) {
        when (val result = signInUseCase(email, password)) {
            is AppResult.Error -> {
                _authUiState.value = AuthUiState.Error(mapAppErrorToMessage(result.error))
            }

            is AppResult.Success -> {
                val refreshedUser = refreshCurrentUserUseCase()
                if(refreshedUser?.isEmailVerified == true) {
                    _authUiState.value = AuthUiState.Success
                } else {
                    _authUiState.value = AuthUiState.Error("Email not verified")
                }
            }
        }
    }

}