package com.example.mapapp.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mapapp.viewModel.MapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(navController: NavController, mapViewModel: MapViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val itb = LatLng(41.4534265, 2.1837151)
        val cameraPositionState = rememberCameraPositionState{
            position = CameraPosition.fromLatLngZoom(itb, 10f)
        }
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(false) }
        MyDrawer(navController = navController,
            mapViewModel = mapViewModel,
            content ={
                Column {
                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(mapViewModel.getPosition(), 10f)
                    }

                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
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
                                    }
                                )
                            }
                        }
                        val marcadores: List<Marker> by mapViewModel.markers.observeAsState(emptyList())
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
}


/*
 val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    MyDrawer(navController = navController,
        mapViewModel = mapViewModel,
        content ={
        Column {
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(mapViewModel.getPosition(), 10f)
            }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
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
                            }
                        )
                    }
                }
                val marcadores: List<Marker> by mapViewModel.markers.observeAsState(emptyList())
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
 */