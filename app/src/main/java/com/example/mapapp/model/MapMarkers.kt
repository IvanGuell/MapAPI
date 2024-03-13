import com.google.android.gms.maps.model.LatLng
data class Markers(
        val position: LatLng,
        val title: String,
        val snippet: String
)
