package com.example.mapapp.navigate

sealed class Routes(val route:String) {
    object MapScreen:Routes("mapscreen")
    object List:Routes("list")
    object DetailScreen:Routes("detail_screen/{title}")
    object GalleryScreen:Routes("galery_screen")
    object TakePhotoScreen:Routes("take_photo_screen")
    object AddMarkerScreen:Routes("add_marker_screen")

    object EditScreen:Routes("edit_screen/{title}")


}