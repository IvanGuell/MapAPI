package com.example.mapapp.viewmodel

import MapMarkers
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapapp.firebase.Repository
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MapViewModel : ViewModel() {


    private val _markerList = MutableLiveData<List<MapMarkers>>()
    val markerList: LiveData<List<MapMarkers>> = _markerList

    private val _actualMarker = MutableLiveData<MapMarkers>()
    val actualMarker: LiveData<MapMarkers> = _actualMarker

    private val _markerTitle = MutableLiveData<String>()
    val markerTitle: LiveData<String> = _markerTitle

    private var position = LatLng(41.4534265, 2.1837151)

    private val _searchedMarkers = MutableLiveData<List<Marker>>()
    val searchedMarkers: LiveData<List<Marker>> = _searchedMarkers

    private val _cameraPermissionGranted = MutableLiveData(false)
    val cameraPermissionGranted = _cameraPermissionGranted

    private val _shouldShowPermissionRationale = MutableLiveData(false)
    val shouldShowPermissionRationale = _shouldShowPermissionRationale

    private val _showPermissionDenied = MutableLiveData(false)
    val showPermissionDenied = _showPermissionDenied

    private var _showBottomSheet = MutableLiveData(false)
    val showBottomSheet = _showBottomSheet

    private val _photoSaved = MutableLiveData<Bitmap>()
    val photoSaved = _photoSaved

    private val _photoTaken = MutableLiveData<Bitmap?>()
    val photoTaken: LiveData<Bitmap?> = _photoTaken

    private val _isMarkerSaved = MutableLiveData(false)
    val isMarkerSaved: LiveData<Boolean> = _isMarkerSaved

    private val _navigationControl = MutableLiveData(false)
    val navigationControl = _navigationControl
    private val _titleText = MutableLiveData<String>("")
    val titleText: LiveData<String> = _titleText

    private val _snippetText = MutableLiveData<String>("")
    val snippetText: LiveData<String> = _snippetText

    private val repository = Repository()

    private val _photoLink = MutableLiveData<String>("")
    val photoLink = _photoLink

    fun saveMarker(marker: MapMarkers) {
        repository.addMarker(marker)
    }

    fun getMarkers() {
        repository.getMarkers().addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore Error", error.message.toString())
                return@addSnapshotListener
            }
            val tempList = mutableListOf<MapMarkers>()
            for (dc: DocumentChange in value?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    val newMarker = dc.document.toObject(MapMarkers::class.java)
                    newMarker.uid = dc.document.id
                    tempList.add(newMarker)
                }
            }
            _markerList.value = tempList
        }
    }

    fun getMarker(markerId: String){
        repository.getMarker(markerId).addSnapshotListener { value, error ->
            if (error != null) {
                Log.w("MarkerRepository", "Listen failed.", error)
                return@addSnapshotListener
            }
            if (value != null && value.exists()) {
                val marker = value.toObject(MapMarkers::class.java)
                if (marker != null) {
                    _actualMarker.value = marker!!
                    _markerTitle.value = _actualMarker.value!!.title
                } else {
                    Log.d("MarkerRepository", "Current data: null")
                }
            }
        }
    }


    fun uploadImage(imageUri: Uri) {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storage = FirebaseStorage.getInstance().getReference("images/$fileName")
        storage.putFile(imageUri)
            .addOnSuccessListener {
                storage.downloadUrl.addOnSuccessListener {
                    _photoLink.value = it.toString()
                    Log.i("IMAGEN", it.toString())
                }
                Log.d("Upload", "Image uploaded")
            }
            .addOnFailureListener {
                Log.e("Upload", "Error uploading image")
            }
    }

    fun setTitleText(text: String) {
        _titleText.value = text
    }

    fun setSnippetText(text: String) {
        _snippetText.value = text
    }

    fun resetInputFields() {
        _titleText.value = ""
        _snippetText.value = ""
        _photoTaken.value = null
    }
    fun saveMarker() {
        _isMarkerSaved.value = true
    }

    fun resetMarkerSaved() {
        _isMarkerSaved.value = false
    }

    fun setPhotoTaken(photo: Bitmap?) {
        _photoTaken.value = photo
    }
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

    fun setShowState(show: Boolean){
        _showBottomSheet.value = show

    }

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

}




data class Marker(
    val position: LatLng,
    val title: String,
    val snippet: String,
    val photo: Bitmap? = null
)