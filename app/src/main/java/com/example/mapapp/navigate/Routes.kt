package com.example.mapapp.navigate

sealed class Routes(val route:String) {
    object MapScreen:Routes("mapscreen")
    object Lista:Routes("list")
    object PositionMarker:Routes("position_marker")
}