package com.alezandrow.simplecleanarchitecture.presentation.state

import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser

sealed class SessionState {
    data object Loading : SessionState()
    data object Unauthenticated : SessionState()
    data class Authenticated(val user: AuthUser) : SessionState()
}