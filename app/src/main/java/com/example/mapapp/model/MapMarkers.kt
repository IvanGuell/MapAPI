import com.google.android.gms.maps.model.LatLng

data class MapMarkers(
        var id: String,
        var position: LatLong,
        var title: String,
        var snippet: String,
        var photo: String?

){
    constructor(): this("", LatLong(0.0, 0.0), "", "", "")
}

data class LatLong(
    var latitude: Double,
    var longitude: Double
) {
    constructor(): this(0.0, 0.0)
}
