package com.example.mapapp.navigate

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Logout
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
                        "Map Screen"
                )

                object Listar : DrawerScreens
                        (
                        Routes.List.route,
                        Icons.Filled.List,
                        "List SCreen"
                )

                object PositionMarker : DrawerScreens
                        (
                        Routes.DetailScreen.route,
                        Icons.Filled.FavoriteBorder,
                        "PScreen"
                )

                object Logout : DrawerScreens
                        (
                        Routes.LoginScreen.route,
                        Icons.Filled.Logout,
                        "Logout"
                )
        }
}

