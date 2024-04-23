import com.google.android.gms.maps.model.LatLng

data class MapMarkers(
        var id: String? = null,
        var usId: String? = null,
        var position: LatLong,
        var title: String,
        var snippet: String,
        var photo: String? = null

){
    constructor(): this(null, null, LatLong(0.0, 0.0), "", "", null)
}

data class LatLong(
    var latitude: Double,
    var longitude: Double
) {
    constructor(): this(0.0, 0.0)
}
