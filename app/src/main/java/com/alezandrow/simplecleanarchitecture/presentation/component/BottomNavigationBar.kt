package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.alezandrow.simplecleanarchitecture.presentation.navigation.destination.Destination
import com.alezandrow.simplecleanarchitecture.presentation.navigation.destination.NavItem

@Composable
fun BottomNavigationBar(navController: NavController, modifier: Modifier = Modifier) {

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    val routes = listOf(
        NavItem(
            title = "Home",
            icon = Icons.Default.Home,
            route = Destination.HomeRoute
        ),
        NavItem(
            title = "Profile",
            icon = Icons.Default.Person,
            route = Destination.ProfileRoute
        )
    )


    BottomAppBar {
        routes.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(item.route::class)
                } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title
                    )
                },
            )
        }
    }
}