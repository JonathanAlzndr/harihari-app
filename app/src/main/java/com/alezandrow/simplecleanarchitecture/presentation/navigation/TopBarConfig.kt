package com.alezandrow.simplecleanarchitecture.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.alezandrow.simplecleanarchitecture.R
import com.alezandrow.simplecleanarchitecture.presentation.navigation.destination.Destination

sealed interface TopBarConfig {

    val title: String

    data class Back(
        override val title: String
    ) : TopBarConfig

    data class Default(
        override val title: String
    ) : TopBarConfig
}

@Composable
fun topBarConfig(
    destination: NavDestination?
): TopBarConfig? {
    return when {
        destination?.hasRoute(Destination.HomeRoute::class) == true -> {
            TopBarConfig.Default(
                title = stringResource(R.string.home)
            )
        }

        destination?.hasRoute(Destination.ProfileRoute::class) == true -> {
            TopBarConfig.Default(
                title = stringResource(R.string.profile)
            )
        }

        destination?.hasRoute(Destination.UpdatePasswordRoute::class) == true -> {
            TopBarConfig.Back(
                title = stringResource(R.string.update_password)
            )
        }

        destination?.hasRoute(Destination.AddTaskRoute::class) == true -> {
            TopBarConfig.Back(
                title = stringResource(R.string.add_task)
            )
        }

        else -> null
    }
}