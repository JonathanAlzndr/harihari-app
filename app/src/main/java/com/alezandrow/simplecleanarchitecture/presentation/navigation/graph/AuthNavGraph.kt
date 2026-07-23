package com.alezandrow.simplecleanarchitecture.presentation.navigation.graph

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.alezandrow.simplecleanarchitecture.presentation.navigation.destination.Destination
import com.alezandrow.simplecleanarchitecture.presentation.screen.login.LoginScreen
import com.alezandrow.simplecleanarchitecture.presentation.screen.register.RegisterScreen
import com.alezandrow.simplecleanarchitecture.presentation.screen.reset_password.ResetPasswordScreen
import com.alezandrow.simplecleanarchitecture.presentation.screen.verify.VerifyEmailScreen

@Composable
fun AuthNavGraph(
    snackbarHostState: SnackbarHostState,
    initialRoute: Destination = Destination.LoginRoute
) {

    val navController = rememberNavController()

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->

        NavHost(navController = navController, startDestination = initialRoute) {

            composable<Destination.LoginRoute> {
                LoginScreen(
                    snackbarHostState = snackbarHostState,
                    navigateToRequestResetPassword = { navController.navigate(Destination.ResetPasswordRoute) },
                    navigateToRegister = { navController.navigate(Destination.RegisterRoute) },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            composable<Destination.RegisterRoute> {
                RegisterScreen(
                    modifier = Modifier.padding(innerPadding),
                    navigateToLogin = { navController.navigate(Destination.LoginRoute) },
                    navigateToVerify = { email ->
                        navController.navigate(Destination.VerifyRoute(email))
                    },
                )
            }

            composable<Destination.VerifyRoute> { backStackEntry ->
                val verifyRouteEmail: Destination.VerifyRoute = backStackEntry.toRoute()
                VerifyEmailScreen(email = verifyRouteEmail.email)
            }

            composable<Destination.ResetPasswordRoute> {
                ResetPasswordScreen(
                    snackbarHostState = snackbarHostState,
                    Modifier.padding(innerPadding)
                )
            }

        }
    }
}