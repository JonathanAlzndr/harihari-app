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
    data class VerifyRoute(val email: String) : Destination
    @Serializable
    data object ResetPasswordRoute: Destination
    @Serializable
    data object ProfileRoute : Destination
    @Serializable
    data object UpdatePasswordRoute : Destination
    @Serializable
    data object CameraRoute : Destination
}