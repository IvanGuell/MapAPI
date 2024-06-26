data class MapMarkers(
    var id: String? = null,
    var userId: String? = null,
    var position: LatLong,
    var title: String,
    var snippet: String,
    var icon: String? = null,
    var photo: String? = null

){
    constructor(): this(null, null, LatLong(), "", "", null, null)
}

data class LatLong(
    var latitude: Double,
    var longitude: Double
) {
    constructor(): this(0.0, 0.0)
}
