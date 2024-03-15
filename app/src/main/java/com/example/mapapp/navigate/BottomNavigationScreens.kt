package com.example.mapapp.navigate

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(
        val route: String,
        val title: String
) {
        sealed class DrawerScreens(
                route: String,
                val icon: ImageVector,
                title: String
        ) : Screens(route, title) {

                object Mapa : DrawerScreens
                        (
                        Routes.MapScreen.route,
                        Icons.Filled.Home,
                        "MapFlurry"
                )

                object Listar : DrawerScreens
                        (
                        Routes.Lista.route,
                        Icons.Filled.List,
                        "My flurry points"
                )

                object PositionMarker : DrawerScreens
                        (
                        Routes.PositionMarker.route,
                        Icons.Filled.FavoriteBorder,
                        "Add flurry point"
                )
        }
}

val screensFromDrawer = listOf(
        Screens.DrawerScreens.Mapa,
        Screens.DrawerScreens.Listar,
        Screens.DrawerScreens.PositionMarker
)