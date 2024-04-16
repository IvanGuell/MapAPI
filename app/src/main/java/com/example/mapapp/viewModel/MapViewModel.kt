package com.example.mapapp.viewmodel

import MapMarkers
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapapp.firebase.Repository
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore


class MapViewModel : ViewModel() {

    private val database = FirebaseFirestore.getInstance()


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


    fun getUsers() {
        Repository.getUsers().addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore Error", error.message.toString())
                return@addSnapshotListener
            }
            val tempList = mutableListOf<MapMarkers>()
            for (dc: DocumentChange in value?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    val newUser = dc.document.toObject(MapMarkers::class.java)
                    newUser.uid = dc.document.id
                    tempList.add(newUser)
                }
            }
            _userList.value = tempList
        }
    }

    fun getUser(userId: String){
        Repository.getUser(userId).addSnapShotListener { value, error ->
            if (error != null) {
                Log.w("UserRepository", "Listen failed.", error)
                return@addSnapshotListener
            }
            if (value != null && value.exists()) {
                val user = value.toObject(User::class.java)
                if (user != null) {
                    _user.userId = userId
                }
                _actualUser.value = user
                _userName.value = _actualUser.value!!.age.toString()
            } else {
                Log.d("UserRepository", "Current data: null")
            }
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