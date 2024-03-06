package com.example.mapapp.navigate

sealed class Routes(val route:String) {
    object MapScreen: Routes("map_screen")
    object MarksScreen: Routes("marks_screen")

}