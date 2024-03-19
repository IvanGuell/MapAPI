package com.example.mapapp.navigate

sealed class Routes(val route:String) {
    object MapScreen:Routes("mapscreen")
    object Lista:Routes("list")
    object PositionMarker:Routes("position_marker")
    object CameraScreen:Routes("camera_screen")
    object TakePhotoScreen:Routes("take_photo_screen")
    object AddMarkerScreen:Routes("add_marker_screen")


}