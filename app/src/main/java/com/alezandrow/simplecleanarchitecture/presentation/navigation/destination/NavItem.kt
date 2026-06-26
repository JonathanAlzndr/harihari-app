package com.alezandrow.simplecleanarchitecture.presentation.navigation.destination

import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val title: String,
    val icon: ImageVector,
    val route: Destination
)