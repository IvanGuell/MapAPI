package com.example.mapapp.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.mapapp.viewmodel.MapViewModel

@Composable
fun PositionMarker(
    navigationController: NavHostController,
    mapViewModel: MapViewModel
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray))
    {
        Text(text = "HelloWorld")

    }
}