package com.alezandrow.simplecleanarchitecture.presentation.state

sealed class SessionState<out T> {
    data object Loading : SessionState<Nothing>()
    data object Unauthenticated : SessionState<Nothing>()
    data class Authenticated<T>(val data: T) : SessionState<T>()
}