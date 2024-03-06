package com.example.mapapp.navigate


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationScreens(val route: String, val icon: ImageVector, val label: String) {
        object Home:BottomNavigationScreens(Routes.MapScreen.route, Icons.Filled.Home, "Home")
        object Favorite:BottomNavigationScreens(Routes.MarksScreen.route, Icons.Filled.FavoriteBorder, "Favorite")
}



