package com.alezandrow.simplecleanarchitecture.presentation.screen.reset_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.RequestPasswordResetUseCase
import com.alezandrow.simplecleanarchitecture.domain.validation.ValidationResult
import com.alezandrow.simplecleanarchitecture.domain.validation.validator.ValidateEmailUseCase
import com.alezandrow.simplecleanarchitecture.presentation.state.ResetPasswordFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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

    fun onChangeEmail(newEmail: String) {
        _emailFormState.update { it.copy(email = newEmail) }
    }

    fun requestPasswordReset() {
        viewModelScope.launch {
            val emailResult = validateEmailUseCase(_emailFormState.value.email)
            if (emailResult is ValidationResult.Error) {
                _emailFormState.update {
                    it.copy(emailError = (emailResult.message))
                }
            }
            requestPasswordResetUseCase(_emailFormState.value.email)
        }
    }

}