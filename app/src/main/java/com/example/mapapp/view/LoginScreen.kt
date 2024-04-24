package com.example.mapapp.view

import androidx.navigation.NavController
import com.example.mapapp.viewmodel.MapViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.mapapp.model.UserPrefs
import com.example.mapapp.navigate.Routes

@Composable
fun LoginScreen(navController: NavController, mapViewModel: MapViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val userPrefs = UserPrefs(context)
    val storedUserData = userPrefs.getUserData.collectAsState(initial = emptyList())

    if (storedUserData.value.isNotEmpty() && storedUserData.value[0] != ""
        && storedUserData.value[1] != "") {
        mapViewModel.modifyProcesssing()
        mapViewModel.login(storedUserData.value[0], storedUserData.value[1])
        if (goToNext) {
            navigationController.navigate(Routes.MapScreen.route)
        }
    }
    Column {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )
        Button(onClick = {
            mapViewModel.login(email, password, navController)
            navController.navigate(Routes.MapScreen.route)

        })
        {
            Text("Login")
        }
        Button(onClick = { navController.navigate(Routes.RegisterScreen.route) }) {
            Text("Register")
        }
    }
}