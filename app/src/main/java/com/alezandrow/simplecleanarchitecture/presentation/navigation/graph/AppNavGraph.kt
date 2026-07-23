package com.alezandrow.simplecleanarchitecture.presentation.navigation.graph

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alezandrow.simplecleanarchitecture.domain.entities.user.AuthUser
import com.alezandrow.simplecleanarchitecture.presentation.navigation.AppNavViewModel
import com.alezandrow.simplecleanarchitecture.presentation.navigation.destination.Destination
import com.alezandrow.simplecleanarchitecture.presentation.state.SessionState

@Composable
fun AppNavGraph(
    appNavViewModel: AppNavViewModel,
) {
    val sessionState by appNavViewModel.sessionState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    when (val state = sessionState) {
        is SessionState.Authenticated<AuthUser> -> {
            if (state.data.isEmailVerified) MainNavGraph(snackbarHostState)
            else AuthNavGraph(
                snackbarHostState = snackbarHostState,
                initialRoute = Destination.VerifyRoute(state.data.email),
            )
        }

        SessionState.Loading -> {}
        SessionState.Unauthenticated -> AuthNavGraph(snackbarHostState = snackbarHostState)
    }

}