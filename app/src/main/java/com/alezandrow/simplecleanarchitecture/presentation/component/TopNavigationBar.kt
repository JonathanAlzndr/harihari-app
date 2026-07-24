package com.alezandrow.simplecleanarchitecture.presentation.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alezandrow.simplecleanarchitecture.presentation.icon.logout_icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(onActionClick: () -> Unit, modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Text(
                text = "App"
            )
        },
        modifier = modifier,
        actions = {
            IconButton(
                onClick = onActionClick
            ) {
                Icon(
                    imageVector = logout_icon,
                    contentDescription = ""
                )
            }
        },
    )
}