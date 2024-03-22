package com.example.mapapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.navigation.NavController
import com.example.mapapp.MainActivity
import com.example.mapapp.MyDrawer
import com.example.mapapp.viewmodel.MapViewModel
import com.example.mapapp.viewmodel.Marker
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavController,
    mapViewModel: MapViewModel
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val fusedLocationProviderClient =
        remember { LocationServices.getFusedLocationProviderClient(context) }
    var lastKnownLocation by remember { mutableStateOf<Location?>(null) }
    var deviceLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    val cameraPositionState =
        rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(deviceLatLng, 18f) }
    val locationResult = fusedLocationProviderClient.getCurrentLocation(100, null)

    locationResult.addOnCompleteListener(context as MainActivity) { task ->
        if (task.isSuccessful) {
            lastKnownLocation = task.result
            deviceLatLng = LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
            cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLatLng, 18f)
        } else {
            Log.e("Error", "Exception: %s", task.exception)
        }

    }


        MyDrawer(navController = navController,
            mapViewModel = mapViewModel,
            content = {
                Column {
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(mapViewModel.getPosition(), 10f)
                    }

                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(isMyLocationEnabled = true),
                        onMapLongClick = {
                            mapViewModel.changePosition(it)
                            showBottomSheet = true
                        }
                    ) {
                        if (showBottomSheet) {
                            ModalBottomSheet(
                                onDismissRequest = {
                                    showBottomSheet = false
                                },
                                sheetState = sheetState
                            ) {

                                AddMarkerScreen(
                                    mapViewModel = mapViewModel,
                                    onCloseBottomSheet = {
                                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                                            if (!sheetState.isVisible) {
                                                showBottomSheet = false
                                            }
                                        }
                                    },
                                    navController = navController

                                )
                            }
                        }
                        val marcadores: List<Marker> by mapViewModel.markers.observeAsState(
                            emptyList()
                        )
                        marcadores.forEach { marker ->
                            Marker(
                                state = MarkerState(marker.position),
                                title = marker.title,
                                snippet = marker.snippet
                            )
                        }
                    }
                }
            }
        )
    }


