package com.example.mapapp

sealed class Routes(val route:String) {
    object MapScreen: Routes("map_screen")
}