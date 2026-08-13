package com.alezandrow.simplecleanarchitecture.presentation.screen.reset_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.RequestPasswordResetUseCase
import com.alezandrow.simplecleanarchitecture.domain.validation.ValidationResult
import com.alezandrow.simplecleanarchitecture.domain.validation.validator.ValidateEmailUseCase
import com.alezandrow.simplecleanarchitecture.presentation.state.AuthEvent
import com.alezandrow.simplecleanarchitecture.presentation.state.ResetPasswordFormState
import com.alezandrow.simplecleanarchitecture.util.mapAppErrorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val requestPasswordResetUseCase: RequestPasswordResetUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase
) : ViewModel() {

    private var _emailFormState = MutableStateFlow(ResetPasswordFormState())
    val emailFormState = _emailFormState.asStateFlow()

    private var _uiEvent = MutableSharedFlow<AuthEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onChangeEmail(newEmail: String) {
        _emailFormState.update { it.copy(email = newEmail) }
    }

    fun requestPasswordReset() {
        viewModelScope.launch {
            val email = _emailFormState.value.email
            when (val emailResult = validateEmailUseCase(email)) {
                is ValidationResult.Error -> {
                    _emailFormState.update {
                        it.copy(
                            emailError = emailResult.message
                        )
                    }
                }
                ValidationResult.Success -> {
                    when (val requestReset = requestPasswordResetUseCase(email)) {
                        is AppResult.Success<*> -> {
                            _uiEvent.emit(
                                AuthEvent.ShowSnackbar(
                                    requestReset.data.toString()
                                )
                            )
                        }
                        is AppResult.Error -> {
                            _uiEvent.emit(
                                AuthEvent.ShowSnackbar(
                                    mapAppErrorToMessage(requestReset.error)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}