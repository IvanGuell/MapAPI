package com.example.mapapp.viewmodel

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

    private val _cameraPermissionGranted = MutableLiveData(false)
    val cameraPermissionGranted = _cameraPermissionGranted

    private val _shouldShowPermissionRationale = MutableLiveData(false)
    val shouldShowPermissionRationale = _shouldShowPermissionRationale

    private val _showPermissionDenied = MutableLiveData(false)
    val showPermissionDenied = _showPermissionDenied

    fun setCameraPermissionGranted(granted: Boolean) {
        _cameraPermissionGranted.value = granted
    }

    fun setShouldShowPermissionRationale(should: Boolean) {
        _shouldShowPermissionRationale.value = should
    }
    fun setShowPermissionDenied(denied: Boolean) {
        _showPermissionDenied.value = denied
    }

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

data class Marker(
    val position: LatLng,
    val title: String,
    val snippet: String)