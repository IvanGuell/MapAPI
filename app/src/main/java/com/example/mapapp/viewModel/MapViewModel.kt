package com.example.mapapp.viewmodel

import MapMarkers
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.mapapp.R
import com.example.mapapp.firebase.Repository
import com.example.mapapp.model.User
import com.example.mapapp.model.UserPrefs
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MapViewModel : ViewModel() {

    val markerIcons = listOf(R.drawable.aeropuerto,R.drawable.gas, R.drawable.cine, R.drawable.supermercado, R.drawable.uni)
    var selectedIcon = MutableLiveData<String>()




    private val authenticator = FirebaseAuth.getInstance()

    private val _goToNext = MutableLiveData(false)
    val goToNext = _goToNext

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> = _userId

    private val _loggedUser = MutableLiveData<String>()
    val loggedUser: LiveData<String> = _loggedUser

    private val _showProgressBar = MutableLiveData(false)
    val showProgressBar = _showProgressBar

    private val _rememberMe = MutableLiveData(false)
    val rememberMe = _rememberMe


    private val _showToast = MutableLiveData(false)
    val showToast = _showToast

    private val _userList = MutableLiveData<List<User>>()
    val userList: LiveData<List<User>> = _userList


    private val _markerList = MutableLiveData<List<MapMarkers>>()
    val markerList: LiveData<List<MapMarkers>> = _markerList

    private val _actualMarker = MutableLiveData<MapMarkers>()
    val actualMarker: LiveData<MapMarkers> = _actualMarker

    private val _markerTitle = MutableLiveData<String>()
    val markerTitle: LiveData<String> = _markerTitle

    private var position = LatLng(41.4534265, 2.1837151)

    private val _searchedMarkers = MutableLiveData<List<MapMarkers>>()
    val searchedMarkers: LiveData<List<MapMarkers>> = _searchedMarkers

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



    private val _navigationControl = MutableLiveData(false)
    val navigationControl = _navigationControl
    private val _titleText = MutableLiveData<String>("")
    val titleText: LiveData<String> = _titleText

    private val _snippetText = MutableLiveData<String>("")
    val snippetText: LiveData<String> = _snippetText

    private val repository = Repository()


    fun subscribeToMarkers() {
        repository.getMarkers().whereEqualTo("userId", FirebaseAuth.getInstance().currentUser?.uid) .addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore Error", error.message.toString())
                return@addSnapshotListener

            }

            val tempList = mutableListOf<MapMarkers>()
            for (dc: DocumentChange in value?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {

                    val newMarker = dc.document.toObject(MapMarkers::class.java)
                    newMarker.id = dc.document.id

                    tempList.add(newMarker)

                }
            }

            _markerList.postValue(tempList)
        }
    }


    fun subscribeToUsers() {
        repository.getUsers().addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("Firestore Error", error.message.toString())
                return@addSnapshotListener

            }

            val tempList = mutableListOf<User>()
            for (dc: DocumentChange in value?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {

                    val newUser = dc.document.toObject(User::class.java)
                    newUser.userId = dc.document.id

                    tempList.add(newUser)

                }
            }

            _userList.postValue(tempList)
        }
    }
    fun register (username: String, password: String){
        if(password.length < 6) {
            _goToNext.value = false
        } else {
            authenticator.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Register", "User registered")
                        _goToNext.value = true
                    } else {
                        _goToNext.value = false
                        _showToast.value = true
                    }
                        Log.e("Register", "Error registering user ${ task.result }")
                    }
                    modifyProcessing()

        }
    }

    fun login (username: String?, password: String?, navController: NavController, rememberMe: Boolean, userPrefs: UserPrefs, context: Context){
        if (username != null && password != null) {
            authenticator.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _userId.value = authenticator.currentUser?.uid
                        _loggedUser.value = username!!
                        if (rememberMe) {
                            CoroutineScope(Dispatchers.IO).launch {
                                userPrefs.saveUserData(username, password, "y")
                            }
                        }
                        _goToNext.value = true
                    } else {
                        _goToNext.value = false
                    }
                    modifyProcessing()
                }
                .addOnFailureListener { exception ->
                    // Show a Toast with an error message
                    Toast.makeText(context, "La contraseña o el password son incorrectos", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "El campo email y el password no pueden ser nulos", Toast.LENGTH_SHORT).show()
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

    private fun modifyProcessing() {
        _showProgressBar.value = true
    }
    fun uploadImage(bitmap: Bitmap, onSuccess: (String) -> Unit) {

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageRef = FirebaseStorage.getInstance().getReference("images/$fileName")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        storageRef.putBytes(data)
            .addOnSuccessListener {
                Log.d("Upload", "Image uploaded")
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString())
                }
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

//    fun onSearchTextChange(keyword: String) {
//        val allMarkers = _markers.value.orEmpty()
//        if (keyword.isNotBlank()) {
//            val filteredMarkers = allMarkers.filter{ it.title.contains(keyword, ignoreCase = true) }
//            _searchedMarkers.value = filteredMarkers
//        } else {
//            _searchedMarkers.value = allMarkers
//        }
//    }

    fun setShowState(show: Boolean){
        _showBottomSheet.value = show

    }

    fun modifyProcessingPublic() {
        modifyProcessing()
    }
    fun changePosition(positionNueva: LatLng) {
        position = positionNueva
    }

    fun getPosition(): LatLng {
        return position
    }


    fun addMarker(marker: MapMarkers) {
        repository.addMarker(marker)
        subscribeToMarkers()
//        val currentList = _markers.value.orEmpty().toMutableList()
//        currentList.add(marker)
//        _markers.value = currentList
    }

    fun removeMarker(marker: MapMarkers) {
        repository.deleteMarker(marker.id!!)
        subscribeToMarkers()

//        val currentList = _markers.value.orEmpty().toMutableList()
//        currentList.remove(marker)
//        _markers.value = currentList
    }
    fun setShowToast(value: Boolean) {
        _showToast.value = value
    }

    fun logout() {
        authenticator.signOut()
        if (_rememberMe.value == false) {
            _userId.value = ""
            _loggedUser.value = ""
        }
    }

    fun updateMarker(marker: MapMarkers) {
        repository.editMarker(marker)
    }
}




