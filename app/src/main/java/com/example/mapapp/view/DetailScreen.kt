package com.example.mapapp.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.mapapp.viewmodel.MapViewModel

@Composable
fun DetailScreen(
    navController: NavController,
    mapViewModel: MapViewModel
){
    val title = navController.currentBackStackEntry?.arguments?.getString("title")
    val marker = mapViewModel.markers.value?.find { it.title == title }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray))
    {
        Text(text = "Nombre: ${marker?.title}")
        Text(text = "Descripción: ${marker?.snippet}")
        Text(text = "Latitud: ${marker?.position?.latitude}")
        Text(text = "Longitud: ${marker?.position?.longitude}")
        // Aquí debes agregar el código para mostrar la imagen del marcador
        marker?.photo?.let { photo ->
            val imageBitmap = photo.asImageBitmap()
            Image(bitmap = imageBitmap, contentDescription = "Foto del marcador")
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