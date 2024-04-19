data class MapMarkers(
        var mid: String? = null,
        var uid: String,
        var lat: Double,
        var lng: Double,
        var title: String,
        var snippet: String,
        var photo: String?
){

        constructor(): this("", "", 0.0, 0.0, "", "", null)


}
