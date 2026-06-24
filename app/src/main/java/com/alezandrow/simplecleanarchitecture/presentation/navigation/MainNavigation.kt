package com.alezandrow.simplecleanarchitecture.presentation.navigation

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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alezandrow.simplecleanarchitecture.presentation.component.AddTaskDialog
import com.alezandrow.simplecleanarchitecture.presentation.component.TopNavigationBar
import com.alezandrow.simplecleanarchitecture.presentation.screen.home.HomeScreen
import com.alezandrow.simplecleanarchitecture.presentation.screen.home.HomeViewModel

@Composable
fun MainNavigation(viewModel: HomeViewModel = hiltViewModel()) {

    var showDialog by remember { mutableStateOf(false) }
    val navController = rememberNavController()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task"
                )
            }
        },
        topBar = {
            TopNavigationBar(onActionClick = viewModel::signOut)
        }
    ) { innerPadding ->

        NavHost(navController = navController, startDestination = Destination.HomeRoute) {
            composable<Destination.HomeRoute> {
                HomeScreen(Modifier.padding(innerPadding), viewModel)
                if (showDialog) {
                    AddTaskDialog(
                        onConfirmation = viewModel::addNewTask,
                        onDismissRequest = { showDialog = false }
                    )
                }
            }
        }

    }
}