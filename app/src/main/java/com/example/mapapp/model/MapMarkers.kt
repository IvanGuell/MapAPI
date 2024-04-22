import com.google.android.gms.maps.model.LatLng

data class MapMarkers(
        var id: String,
        var position: LatLng,
        var title: String,
        var snippet: String,
        var photo: String?

)