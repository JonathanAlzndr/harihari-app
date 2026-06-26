package com.alezandrow.simplecleanarchitecture.presentation.navigation.destination

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination {
    @Serializable
    object HomeRoute : Destination

    @Serializable
    object LoginRoute : Destination

    @Serializable
    object RegisterRoute : Destination

    @Serializable
    object Root : Destination

    @Serializable
    data class Verify(val email: String) : Destination
}