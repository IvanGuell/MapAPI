package com.example.mapapp.view

import MapViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapScreen(navController: NavController, viewModel: com.example.mapapp.viewModel.MapViewModel, onMarkerAdded: () -> Unit) {
    val viewModel: MapViewModel = viewModel()
    val selectedMarkerPosition by viewModel.selectedMarkerPosition.observeAsState()
    val bottomSheetState = rememberBottomSheetScaffoldState(bottomSheetState = rememberBottomSheetState(
        BottomSheetValue.Collapsed))

    LaunchedEffect(bottomSheetState.bottomSheetState) {
        bottomSheetState.bottomSheetState.collapse()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val itb = LatLng(41.4534265, 2.1837151)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(itb, 10f)
        }

        BottomSheetScaffold(
            sheetContent = {
                if (selectedMarkerPosition != null) {
                    AddMarkerBottomSheet(
                        onAddMarker = {
                            viewModel.onAddMarkerButtonClick()
                            onMarkerAdded() // Notify the parent when the marker is added
                        }
                    )
                }
            },
            scaffoldState = bottomSheetState
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { viewModel.onMapLongClick(it) },
                onMapLongClick = { viewModel.onMapLongClick(it) }
            ) {
                if (selectedMarkerPosition != null) {
                    Marker(
                        state = MarkerState(position = selectedMarkerPosition!!),
                        title = "Custom Marker",
                        snippet = "Description"
                    )
                }
            }
        }
    }
}

@Composable
fun AddMarkerBottomSheet(
    onAddMarker: () -> Unit
) {
    var markerTitle by remember { mutableStateOf("") }
    var markerSnippet by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value = markerTitle,
            onValueChange = { markerTitle = it },
            label = { Text("Marker Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = markerSnippet,
            onValueChange = { markerSnippet = it },
            label = { Text("Marker Snippet") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAddMarker,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Marker")
        }
    }
}
