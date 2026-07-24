package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.alezandrow.simplecleanarchitecture.presentation.icon.logout_icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(
    title: String,
    modifier: Modifier = Modifier,
    onLogoutActionClick: (() -> Unit)? = null,
    onNavigationClick: (() -> Unit)? = null,
    navigationIcon: ImageVector? = null
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if(onNavigationClick != null && navigationIcon != null) {
                IconButton(
                    onClick = onNavigationClick
                ) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = "Navigate Back"
                    )
                }
            }
        },
        modifier = modifier,
        actions = {
            if (onLogoutActionClick != null) {
                IconButton(
                    onClick = onLogoutActionClick
                ) {
                    Icon(
                        imageVector = logout_icon,
                        contentDescription = "Logout"
                    )
                }
            }
        },
    )
}