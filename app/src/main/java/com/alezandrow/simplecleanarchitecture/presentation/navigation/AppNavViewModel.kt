package com.alezandrow.simplecleanarchitecture.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.ObserveCurrentUserUseCase
import com.alezandrow.simplecleanarchitecture.presentation.state.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppNavViewModel @Inject constructor(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
) : ViewModel() {

    val sessionState: StateFlow<SessionState<AuthUser>> =
        observeCurrentUserUseCase()
            .map { user ->
                if (user == null) {
                    SessionState.Unauthenticated
                } else {
                    SessionState.Authenticated(data = user)
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                initialValue = SessionState.Loading
            )

}