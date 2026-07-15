package com.alezandrow.simplecleanarchitecture.presentation.navigation.graph

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alezandrow.simplecleanarchitecture.presentation.component.AddTaskDialog
import com.alezandrow.simplecleanarchitecture.presentation.component.BottomNavigationBar
import com.alezandrow.simplecleanarchitecture.presentation.component.TopNavigationBar
import com.alezandrow.simplecleanarchitecture.presentation.navigation.destination.Destination
import com.alezandrow.simplecleanarchitecture.presentation.screen.camera.CameraScreen
import com.alezandrow.simplecleanarchitecture.presentation.screen.home.HomeScreen
import com.alezandrow.simplecleanarchitecture.presentation.screen.home.HomeViewModel
import com.alezandrow.simplecleanarchitecture.presentation.screen.profile.ProfileScreen
import com.alezandrow.simplecleanarchitecture.presentation.screen.update_password.UpdatePasswordScreen

@Composable
fun MainNavGraph(viewModel: HomeViewModel = hiltViewModel()) {

    var showDialog by remember { mutableStateOf(false) }
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val isAtHome = currentDestination?.hierarchy?.any {
        it.hasRoute(Destination.HomeRoute::class)
    } == true

    Scaffold(
        floatingActionButton = {
            if (isAtHome) {
                FloatingActionButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task"
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(navController)
        },
        topBar = {
            TopNavigationBar(viewModel::signOut)
        }
    ) { innerPadding ->

        if (showDialog) {
            AddTaskDialog(
                onConfirmation = viewModel::addNewTask,
                onDismissRequest = { showDialog = false }
            )
        }

        NavHost(navController = navController, startDestination = Destination.HomeRoute) {
            composable<Destination.HomeRoute> {
                HomeScreen(Modifier.padding(innerPadding), viewModel)
            }

            composable<Destination.ProfileRoute> {
                ProfileScreen(
                    navigateToChangePassword = { navController.navigate(Destination.UpdatePasswordRoute) },
                    modifier = Modifier.padding(16.dp)
                )
            }

            composable<Destination.UpdatePasswordRoute> {
                UpdatePasswordScreen(modifier = Modifier.padding(innerPadding))
            }

            composable<Destination.CameraRoute> {
                CameraScreen(modifier = Modifier.padding(innerPadding))
            }

        }
    }
}