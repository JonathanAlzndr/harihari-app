package com.alezandrow.simplecleanarchitecture.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.DeleteAccountUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.ObserveCurrentUserUseCase
import com.alezandrow.simplecleanarchitecture.presentation.state.AuthUiState
import com.alezandrow.simplecleanarchitecture.presentation.util.mapAppErrorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase
) : ViewModel() {

    private val _authUiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val authUiState = _authUiState.asStateFlow()

    fun deleteAccount() {
        viewModelScope.launch {
            when (val result = deleteAccountUseCase()) {
                is AppResult.Error -> _authUiState.update {
                    AuthUiState.Error(
                        mapAppErrorToMessage(result.error)
                    )
                }

                is AppResult.Success<String> -> _authUiState.update {
                    AuthUiState.Success
                }
            }
        }
    }
}