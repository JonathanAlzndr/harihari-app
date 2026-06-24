package com.alezandrow.simplecleanarchitecture.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alezandrow.simplecleanarchitecture.presentation.screen.login.LoginScreen
import com.alezandrow.simplecleanarchitecture.presentation.screen.register.RegisterScreen

@Composable
fun AuthNavigation() {

    val navController = rememberNavController()

    Scaffold() { innerPadding ->

        NavHost(navController = navController, startDestination = Destination.RegisterRoute) {

            composable<Destination.LoginRoute> {
                LoginScreen(modifier = Modifier.padding(innerPadding))
            }

            composable<Destination.RegisterRoute> {
                RegisterScreen(
                    modifier = Modifier.padding(innerPadding),
                    navigateToLogin = { navController.navigate(Destination.LoginRoute) }
                )
            }

        }
    }
}