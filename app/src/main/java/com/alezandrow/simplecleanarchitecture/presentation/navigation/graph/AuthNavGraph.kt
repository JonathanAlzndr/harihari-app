package com.alezandrow.simplecleanarchitecture.presentation.navigation.graph

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.alezandrow.simplecleanarchitecture.presentation.navigation.destination.Destination
import com.alezandrow.simplecleanarchitecture.presentation.screen.login.LoginScreen
import com.alezandrow.simplecleanarchitecture.presentation.screen.register.RegisterScreen
import com.alezandrow.simplecleanarchitecture.presentation.screen.verify.VerifyEmailScreen

@Composable
fun AuthNavGraph(initialRoute: Destination = Destination.RegisterRoute) {

    val navController = rememberNavController()

    Scaffold { innerPadding ->

        NavHost(navController = navController, startDestination = initialRoute) {

            composable<Destination.LoginRoute> {
                LoginScreen(modifier = Modifier.padding(innerPadding))
            }

            composable<Destination.RegisterRoute> {
                RegisterScreen(
                    modifier = Modifier.padding(innerPadding),
                    navigateToLogin = { navController.navigate(Destination.LoginRoute) },
                    navigateToVerify = { email ->
                        navController.navigate(Destination.Verify(email))
                    }
                )
            }

            composable<Destination.Verify> { backStackEntry ->
                val verifyEmail: Destination.Verify = backStackEntry.toRoute()
                VerifyEmailScreen(email = verifyEmail.email)
            }

        }
    }
}