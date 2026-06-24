package com.alezandrow.simplecleanarchitecture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alezandrow.simplecleanarchitecture.domain.usecase.auth.ObserveUserSessionUseCase
import com.alezandrow.simplecleanarchitecture.presentation.state.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val observeUserSessionUseCase: ObserveUserSessionUseCase
) : ViewModel() {

    val sessionState =
        observeUserSessionUseCase()
            .map {
                if (it == null) {
                    SessionState.Unauthenticated
                } else {
                    SessionState.Authenticated(user = it)
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Companion.WhileSubscribed(5000),
                initialValue = SessionState.Loading
            )
}