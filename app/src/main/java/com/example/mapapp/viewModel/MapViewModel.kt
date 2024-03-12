package com.example.mapapp.viewModel

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapViewModel: ViewModel() {
    private val _loading = MutableLiveData(true)
    val loading = _loading

    private val _searchText = MutableLiveData<String>()
    val searchText = _searchText

    private val _show = MutableLiveData<Boolean>()
    val show = _show

    private val _selectedMarkerPosition = MutableLiveData<LatLng?>()
    val selectedMarkerPosition: LiveData<LatLng?> get() = _selectedMarkerPosition

    fun onMapLongClick(position: LatLng) {
        _selectedMarkerPosition.value = position
    }

    fun onAddMarkerButtonClick() {
        _selectedMarkerPosition.value = null
    }
    fun onSearchTextChange(text: String?) {
        text?.let {

            println("Texto de búsqueda: $it")
        }
    }
    fun changeShow() {
        _show.value = show.value != true
    }

    @Composable
    fun CameraCapture(onPhotoCaptured: (Bitmap) -> Unit) {
    }


}