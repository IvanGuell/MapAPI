package com.example.sergiitb_pr04_maps_app.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mapapp.viewmodel.MapViewModel
import com.example.mapapp.viewmodel.Marker


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMarkerScreen(
    mapViewModel: MapViewModel,
    onCloseBottomSheet: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var snippet by remember { mutableStateOf("") }

    Surface(color = Color(0xFFFFFFFF)) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = title,
                onValueChange = {
                    title = it
                },
                label = { Text("nombre del lugar") },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = snippet,
                onValueChange = {
                    snippet = it
                },
                label = { Text("breve descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val latLng = mapViewModel.getPosition()
                    val markerToAdd = Marker(latLng, title, snippet)
                    mapViewModel.addMarker(markerToAdd)
                    onCloseBottomSheet()
                },
                colors = ButtonDefaults.buttonColors(Color(0xffFF914D)),
                modifier = Modifier.width(150.dp)
            ) {
                Text("GUARDAR")
            }
        }
    }
}