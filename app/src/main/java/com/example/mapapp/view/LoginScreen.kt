package com.example.mapapp.view

import android.widget.Toast
import androidx.navigation.NavController
import com.example.mapapp.viewmodel.MapViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.mapapp.model.UserPrefs
import com.example.mapapp.navigate.Routes

@Composable
fun LoginScreen(navController: NavController, mapViewModel: MapViewModel) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false)}
    var passwordVisibility by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val userPrefs = UserPrefs(context)
    val storedUserData = userPrefs.getUserData.collectAsState(initial = emptyList())

    val goToNext by mapViewModel.goToNext.observeAsState(false)

    if (storedUserData.value.isNotEmpty() && storedUserData.value[0] != ""
        && storedUserData.value[1] != "" && storedUserData.value[2] == "y") {
        mapViewModel.modifyProcessingPublic()
        mapViewModel.login(storedUserData.value[0], storedUserData.value[1], navController, storedUserData.value[2] == "y", userPrefs, context)
        if (goToNext) {
            navController.navigate(Routes.MapScreen.route)
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
            label = { Text("Password") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle password visibility"
                    )
                }
            }
        )
        Button(onClick = {
            mapViewModel.login(email, password, navController, rememberMe, userPrefs, context)
            if (goToNext) {
                navController.navigate(Routes.MapScreen.route)
            }
        },
            enabled = email.isNotBlank() && password.isNotBlank()) {
            Text("Login")
        }
        Button(onClick = { navController.navigate(Routes.RegisterScreen.route) }) {
            Text("Register")
        }

        Row {
            Checkbox(
                checked = rememberMe,
                onCheckedChange = { rememberMe = it },
                // other parameters...
            )
            Text("Remember me")
        }
    }
}