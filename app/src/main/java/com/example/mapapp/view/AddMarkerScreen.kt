package com.example.mapapp.view


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.mapapp.navigate.Routes
import com.example.mapapp.viewmodel.MapViewModel
import com.example.mapapp.viewmodel.Marker


@Composable
fun AddMarkerScreen(
    navController: NavController,
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
            Button(
                onClick = {
                    navController.navigate(Routes.TakePhotoScreen.route)
                },
                colors = ButtonDefaults.buttonColors(Color(0xffFF914D)),
                modifier = Modifier.width(150.dp)
            ) {
                Text("Camera")
            }
        }
    }
}


@Composable
fun CameraScreen(navController: NavController, mapViewModel: MapViewModel) {
    val context = LocalContext.current
    val isCameraPermissionGranted by mapViewModel.cameraPermissionGranted.observeAsState(false)
    val shouldPermissionRationale by mapViewModel.shouldShowPermissionRationale.observeAsState(false)
    val showPermissionDenied by mapViewModel.showPermissionDenied.observeAsState(false)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {

                mapViewModel.setCameraPermissionGranted(true)
            } else {
                mapViewModel.setShouldShowPermissionRationale(
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.CAMERA
                    )

                )
                if (!shouldPermissionRationale) {
                    Log.i("CameraScreen", "No podemos volver a pedi permisos")
                    mapViewModel.setShowPermissionDenied(true)
                }
            }
        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()

    ) {
        Button(onClick = {
            if (!isCameraPermissionGranted) {
                launcher.launch(Manifest.permission.CAMERA)
            } else {
                navController.navigate(Routes.TakePhotoScreen.route)
            }


        }) {
            Text("Take photo")
        }


    }
    if (showPermissionDenied) {
        PermissionDeclinedScreen()
    }
}

@Composable
fun PermissionDeclinedScreen() {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Permission Required", fontWeight = FontWeight.Bold)
        Text(text = "You need to grant camera permission to take a photo")
        Button(onClick = {
            openAppSettings(context as Activity)

        }) {
            Text(text = "Open Settings")
        }


    }
}


fun openAppSettings(activity: Activity) {
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", activity.packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK

    }
    activity.startActivity(intent)

}