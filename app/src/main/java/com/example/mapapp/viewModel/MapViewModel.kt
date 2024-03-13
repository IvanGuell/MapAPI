package com.example.mapapp.viewModel

import Markers
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

    private var position = LatLng(41.4534265, 2.1837151)
    fun changePosition(positionNueva: LatLng) {
        position = positionNueva
    }

    fun getPosition(): LatLng {
        return position
    }

    private val _markers = MutableLiveData<List<Markers>>()
    val markers: LiveData<List<Markers>> = _markers

    fun addMarker(marker: Markers) {
        val currentList = _markers.value.orEmpty().toMutableList()
        currentList.add(marker)
        _markers.value = currentList
    }

    fun removeMarker(marker: Markers) {
        val currentList = _markers.value.orEmpty().toMutableList()
        currentList.remove(marker)
        _markers.value = currentList
    }

    private val _searchedMarkers = MutableLiveData<List<Markers>>()
    val searchedMarkers: LiveData<List<Markers>> = _searchedMarkers

    fun onSearchTextChange(keyword: String) {
        val allMarkers = _markers.value.orEmpty()
        if (keyword.isNotBlank()) {
            val filteredMarkers = allMarkers.filter{ it.title.contains(keyword, ignoreCase = true) }
            _searchedMarkers.value = filteredMarkers
        } else {
            _searchedMarkers.value = allMarkers
        }
    }


    fun changeShow() {
        _show.value = show.value != true
    }

    @Composable
    fun CameraCapture(onPhotoCaptured: (Bitmap) -> Unit) {
    }


}



/*
package com.example.sergiitb_pr04_maps_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapViewModel : ViewModel() {

    private var position = LatLng(41.4534265, 2.1837151)
    fun changePosition(positionNueva: LatLng) {
        position = positionNueva
    }

    fun getPosition(): LatLng {
        return position
    }

    private val _markers = MutableLiveData<List<Marker>>()
    val markers: LiveData<List<Marker>> = _markers

    fun addMarker(marker: Marker) {
        val currentList = _markers.value.orEmpty().toMutableList()
        currentList.add(marker)
        _markers.value = currentList
    }

    fun removeMarker(marker: Marker) {
        val currentList = _markers.value.orEmpty().toMutableList()
        currentList.remove(marker)
        _markers.value = currentList
    }

    private val _searchedMarkers = MutableLiveData<List<Marker>>()
    val searchedMarkers: LiveData<List<Marker>> = _searchedMarkers

    fun onSearchTextChange(keyword: String) {
        val allMarkers = _markers.value.orEmpty()
        if (keyword.isNotBlank()) {
            val filteredMarkers = allMarkers.filter{ it.title.contains(keyword, ignoreCase = true) }
            _searchedMarkers.value = filteredMarkers
        } else {
            _searchedMarkers.value = allMarkers
        }
    }
}


 */