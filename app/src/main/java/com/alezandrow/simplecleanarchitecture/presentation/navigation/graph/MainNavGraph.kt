package com.alezandrow.simplecleanarchitecture.presentation.navigation.graph

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alezandrow.simplecleanarchitecture.R
import com.alezandrow.simplecleanarchitecture.presentation.component.BottomNavigationBar
import com.alezandrow.simplecleanarchitecture.presentation.component.TopNavigationBar
import com.alezandrow.simplecleanarchitecture.presentation.icon.add_icon
import com.alezandrow.simplecleanarchitecture.presentation.icon.arrow_back_icon
import com.alezandrow.simplecleanarchitecture.presentation.navigation.TopBarConfig
import com.alezandrow.simplecleanarchitecture.presentation.navigation.destination.Destination
import com.alezandrow.simplecleanarchitecture.presentation.navigation.topBarConfig
import com.alezandrow.simplecleanarchitecture.presentation.screen.editor.TaskEditorScreen
import com.alezandrow.simplecleanarchitecture.presentation.screen.home.HomeScreen
import com.alezandrow.simplecleanarchitecture.presentation.screen.home.HomeViewModel
import com.alezandrow.simplecleanarchitecture.presentation.screen.profile.ProfileScreen
import com.alezandrow.simplecleanarchitecture.presentation.screen.update_password.UpdatePasswordScreen

@Composable
fun MainNavGraph(snackbarHostState: SnackbarHostState, viewModel: HomeViewModel = hiltViewModel()) {

    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val topBarConfig = topBarConfig(currentDestination)
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            if (
                currentDestination?.hasRoute(
                    Destination.HomeRoute::class
                ) == true
            ) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(
                            Destination.TaskEditorRoute()
                        )
                    }
                ) {
                    Icon(
                        imageVector = add_icon,
                        contentDescription = stringResource(R.string.add_task)
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            if (
                currentDestination?.hasRoute(
                    Destination.HomeRoute::class
                ) == true ||
                currentDestination?.hasRoute(
                    Destination.ProfileRoute::class
                ) == true
            ) {
                BottomNavigationBar(navController)
            }
        },
        topBar = {
            when (topBarConfig) {
                is TopBarConfig.Back -> {
                    TopNavigationBar(
                        title = topBarConfig.title,
                        onNavigationClick = navController::popBackStack,
                        navigationIcon = arrow_back_icon,
                        onLogoutActionClick = { viewModel.signOut(context) }
                    )
                }

                is TopBarConfig.Default -> {
                    TopNavigationBar(
                        title = topBarConfig.title,
                        onLogoutActionClick = { viewModel.signOut(context) }
                    )
                }

                null -> Unit
            }
        }
    ) { innerPadding ->

        NavHost(navController = navController, startDestination = Destination.HomeRoute) {
            composable<Destination.HomeRoute> {
                HomeScreen(
                    onNavigateToTaskEditor = { taskId ->
                        navController.navigate(
                            Destination.TaskEditorRoute(taskId)
                        )
                    },
                    snackbarHostState = snackbarHostState,
                    viewModel = viewModel,
                    modifier = Modifier.padding(innerPadding),
                )
            }

            composable<Destination.ProfileRoute> {
                ProfileScreen(
                    snackbarHostState = snackbarHostState,
                    navigateToChangePassword = { navController.navigate(Destination.UpdatePasswordRoute) },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            composable<Destination.UpdatePasswordRoute> {
                UpdatePasswordScreen(
                    snackbarHostState = snackbarHostState,
                    Modifier.padding(innerPadding)
                )
            }

            composable<Destination.TaskEditorRoute> { backStackEntry ->
                TaskEditorScreen(
                    navigateBack = { navController.popBackStack() },
                    snackbarHostState = snackbarHostState,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}