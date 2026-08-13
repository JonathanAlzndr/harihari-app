package com.alezandrow.simplecleanarchitecture.presentation.screen.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezandrow.simplecleanarchitecture.common.AppResult
import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.DeleteAccountUseCase
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.ObserveCurrentUserUseCase
import com.alezandrow.simplecleanarchitecture.presentation.state.AuthEvent
import com.alezandrow.simplecleanarchitecture.presentation.state.ProfileUiState
import com.alezandrow.simplecleanarchitecture.util.mapAppErrorToMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<AuthEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val currentUser: StateFlow<ProfileUiState<AuthUser>> =
        observeCurrentUserUseCase()
            .map { user ->
                if (user == null) {
                    ProfileUiState.Error("User not found")
                } else {
                    Log.d(TAG, user.toString())
                    ProfileUiState.Success(user)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = ProfileUiState.Loading
            )

    fun deleteAccount() {
        viewModelScope.launch {
            when (val result = deleteAccountUseCase()) {
                is AppResult.Error -> {
                    _uiEvent.emit(AuthEvent.ShowSnackbar(mapAppErrorToMessage(result.error)))
                }

                is AppResult.Success<*> -> {
                    _uiEvent.emit(AuthEvent.ShowSnackbar(result.data.toString()))
                }
            }
        }
    }

    companion object {
        const val TAG = "ProfileViewModel"
    }

}