package com.alezandrow.simplecleanarchitecture

import androidx.compose.runtime.Composable
import com.alezandrow.simplecleanarchitecture.presentation.navigation.AppNavViewModel
import com.alezandrow.simplecleanarchitecture.presentation.navigation.graph.AppNavGraph
import com.alezandrow.simplecleanarchitecture.presentation.theme.SimpleCleanArchitectureTheme

@Composable
fun App(viewModel: AppNavViewModel) {
    SimpleCleanArchitectureTheme {
        AppNavGraph(viewModel)
    }
}