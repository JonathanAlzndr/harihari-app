package com.alezandrow.simplecleanarchitecture.presentation.navigation.graph

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alezandrow.simplecleanarchitecture.presentation.component.BottomNavigationBar
import com.alezandrow.simplecleanarchitecture.presentation.component.TopNavigationBar
import com.alezandrow.simplecleanarchitecture.presentation.icon.add_icon
import com.alezandrow.simplecleanarchitecture.presentation.navigation.destination.Destination
import com.alezandrow.simplecleanarchitecture.presentation.screen.add_task.AddTaskScreen
import com.alezandrow.simplecleanarchitecture.presentation.screen.home.HomeScreen
import com.alezandrow.simplecleanarchitecture.presentation.screen.home.HomeViewModel
import com.alezandrow.simplecleanarchitecture.presentation.screen.profile.ProfileScreen
import com.alezandrow.simplecleanarchitecture.presentation.screen.update_password.UpdatePasswordScreen

@Composable
fun MainNavGraph(snackbarHostState: SnackbarHostState, viewModel: HomeViewModel = hiltViewModel()) {

    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val isAtHome = currentDestination?.hierarchy?.any {
        it.hasRoute(Destination.HomeRoute::class)
    } == true
    val isAtAddTask = currentDestination?.hierarchy?.any {
        it.hasRoute(Destination.AddTaskRoute::class)
    } == true

    Scaffold(
        floatingActionButton = {
            if (isAtHome) {
                FloatingActionButton(onClick = { navController.navigate(Destination.AddTaskRoute) }) {
                    Icon(
                        imageVector = add_icon,
                        contentDescription = "Add Task"
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            if (!isAtAddTask) {
                BottomNavigationBar(navController)
            }
        },
        topBar = {
            TopNavigationBar(viewModel::signOut)
        }
    ) { innerPadding ->

        NavHost(navController = navController, startDestination = Destination.HomeRoute) {
            composable<Destination.HomeRoute> {
                HomeScreen(
                    snackbarHostState = snackbarHostState,
                    modifier = Modifier.padding(innerPadding),
                    viewModel = viewModel
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

            composable<Destination.AddTaskRoute> {
                AddTaskScreen(
                    navigateBack = { navController.popBackStack() },
                    snackbarHostState = snackbarHostState,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}