package com.alezandrow.simplecleanarchitecture.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination {
    @Serializable
    object HomeRoute: Destination
    @Serializable
    object LoginRoute: Destination
    @Serializable
    object RegisterRoute: Destination
    @Serializable
    object Root: Destination
}