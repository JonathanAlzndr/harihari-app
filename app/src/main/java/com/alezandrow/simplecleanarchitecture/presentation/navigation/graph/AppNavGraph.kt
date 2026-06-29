package com.alezandrow.simplecleanarchitecture.presentation.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.credentials.CredentialManager
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

    when (val state = sessionState) {
        is SessionState.Authenticated<AuthUser> -> {
            if (state.data.isEmailVerified) MainNavGraph()
            else AuthNavGraph(
                initialRoute = Destination.VerifyRoute(state.data.email),
            )
        }

        SessionState.Loading -> {}
        SessionState.Unauthenticated -> AuthNavGraph()
    }

}