package com.alezandrow.simplecleanarchitecture.presentation.screen.update_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.UpdatePasswordUseCase
import com.alezandrow.simplecleanarchitecture.domain.validation.ValidationResult
import com.alezandrow.simplecleanarchitecture.domain.validation.validator.ValidatePasswordUseCase
import com.alezandrow.simplecleanarchitecture.presentation.state.AuthUiState
import com.alezandrow.simplecleanarchitecture.presentation.state.UpdatePasswordFormState
import com.alezandrow.simplecleanarchitecture.presentation.util.mapAppErrorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdatePasswordViewModel @Inject constructor(
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {

    private var _authUiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val authUiState = _authUiState.asStateFlow()

    private val _formState = MutableStateFlow(UpdatePasswordFormState())
    val formState = _formState.asStateFlow()

    fun onCurrentPasswordChanged(newValue: String) {
        _formState.update { it.copy(currentPassword = newValue) }
    }

    fun onNewPasswordChanged(newValue: String) {
        _formState.update { it.copy(newPassword = newValue, newPasswordError = null) }
    }

    fun onConfirmNewPasswordChanged(newValue: String) {
        _formState.update { it.copy(confirmNewPassword = newValue, confirmNewPasswordError = null) }
    }

    fun updatePassword() {
        val currentForm = _formState.value

        val passwordResult = validatePasswordUseCase(currentForm.newPassword)
        val hasPasswordMatch = currentForm.newPassword == currentForm.confirmNewPassword

        if (passwordResult is ValidationResult.Error || !hasPasswordMatch) {
            _formState.update {
                it.copy(
                    newPasswordError = (passwordResult as? ValidationResult.Error)?.message,
                    confirmNewPasswordError = if (!hasPasswordMatch) "Password is not matching" else null
                )
            }
            return
        }

        viewModelScope.launch {
            val result = updatePasswordUseCase(currentForm.currentPassword, currentForm.newPassword)
            if (result is AppResult.Success) _authUiState.value = AuthUiState.Success
            if (result is AppResult.Error) _authUiState.value = AuthUiState.Error(
                mapAppErrorToMessage(result.error)
            )
        }
    }

}