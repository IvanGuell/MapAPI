package com.example.mapapp.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mapapp.viewmodel.MapViewModel



@Composable
fun DetailScreen(
    navController: NavController,
    mapViewModel: MapViewModel
){

    val title = navController.currentBackStackEntry?.arguments?.getString("title")
    val marker = mapViewModel.markerList.value?.find { it.title == title }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray))
    {
        Text(text = "Nombre: ${marker?.title}")
        Text(text = "Descripción: ${marker?.snippet}")

        marker?.photo?.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = "Marker Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Button(onClick = { navController.navigate("edit_screen/${marker?.title}") }) {
            Text("Editar")
        }

        Button(onClick = {
            if (marker != null) {
                mapViewModel.removeMarker(marker)
            }
            navController.popBackStack()
        }) {
            Text("Eliminar")
        }

        Button(onClick = { navController.popBackStack() }) {
            Text("Volver atrás")
        }
    }
}