import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData

class MapViewModel : ViewModel() {
    private val _selectedMarkerPosition = MutableLiveData<LatLng?>()
    val selectedMarkerPosition: LiveData<LatLng?> get() = _selectedMarkerPosition

    fun onMapLongClick(position: LatLng) {
        _selectedMarkerPosition.value = position
    }

    fun onAddMarkerButtonClick() {
        _selectedMarkerPosition.value = null
    }
}