package com.alezandrow.simplecleanarchitecture

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alezandrow.simplecleanarchitecture.presentation.navigation.AuthNavigation
import com.alezandrow.simplecleanarchitecture.presentation.navigation.MainNavigation
import com.alezandrow.simplecleanarchitecture.presentation.state.SessionState

@Composable
fun RootAppNavigation(
    sessionViewModel: SessionViewModel
) {
    val sessionState by sessionViewModel.sessionState.collectAsStateWithLifecycle()

    when (sessionState) {
        is SessionState.Authenticated -> {
            MainNavigation()
        }
        SessionState.Loading -> {}
        SessionState.Unauthenticated -> {
            AuthNavigation()
        }
    }

}