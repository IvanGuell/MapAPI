package com.example.mapapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.mapapp.navigate.Routes
import com.example.mapapp.viewmodel.MapViewModel
import com.example.mapapp.viewmodel.Marker


@Composable
fun EditScreen(
    navController: NavController,
    mapViewModel: MapViewModel
){
    val title = navController.currentBackStackEntry?.arguments?.getString("title")
    val marker = mapViewModel.markers.value?.find { it.title == title }
    var newTitle by remember { mutableStateOf(marker?.title ?: "") }
    var newSnippet by remember { mutableStateOf(marker?.snippet ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray))
    {
        TextField(
            value = newTitle,
            onValueChange = { newTitle = it },
            label = { Text("Nombre") }
        )
        TextField(
            value = newSnippet,
            onValueChange = { newSnippet = it },
            label = { Text("Descripción") }
        )
        Button(onClick = {
            navController.navigate(Routes.TakePhotoScreen.route)
        }) {
            Text("Cambiar foto")
        }
        Button(onClick = {
            if (marker != null) {
                mapViewModel.removeMarker(marker)
                val newPhoto = mapViewModel.photoTaken.value
                val newMarker = Marker(marker.position, newTitle, newSnippet, newPhoto)
                mapViewModel.addMarker(newMarker)
            }
            navController.popBackStack()
        }) {
            Text("Guardar")
        }
    }
}