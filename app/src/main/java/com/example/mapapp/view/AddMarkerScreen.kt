package com.example.mapapp.view


import LatLong
import MapMarkers
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.mapapp.navigate.Routes
import com.example.mapapp.viewmodel.MapViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun AddMarkerScreen(
    navController: NavController,
    mapViewModel: MapViewModel,
    onCloseBottomSheet: () -> Unit
) {

    val photoTaken by mapViewModel.photoTaken.observeAsState()



    var selectedIcon by remember { mutableStateOf(mapViewModel.markerIcons[0]) }
    var expanded by remember { mutableStateOf(false) }


    Surface(color = Color(0xFFFFFFFF)) {
        val context = LocalContext.current
        val isCameraPermissionGranted by mapViewModel.cameraPermissionGranted.observeAsState(false)
        val shouldPermissionRationale by mapViewModel.shouldShowPermissionRationale.observeAsState(false)
        val showPermissionDenied by mapViewModel.showPermissionDenied.observeAsState(false)
        val title by mapViewModel.titleText.observeAsState("")
        val snippet by mapViewModel.snippetText.observeAsState("")

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    mapViewModel.setCameraPermissionGranted(true)
                    navController.navigate(Routes.TakePhotoScreen.route)
                } else {
                    mapViewModel.setShouldShowPermissionRationale(
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            context as Activity,
                            Manifest.permission.CAMERA
                        )

                    )
                    if (!shouldPermissionRationale) {
                        Log.i("CameraScreen", "No podemos volver a pedir permisos")
                        mapViewModel.setShowPermissionDenied(true)
                    }
                }
            }
        )
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = title,
                onValueChange = {
                    mapViewModel.setTitleText(it)
                },
                label = { Text("Nombre") },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = snippet,
                onValueChange = {
                    mapViewModel.setSnippetText(it)
                },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select Icon")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                mapViewModel.markerIcons.forEach { icon ->
                    DropdownMenuItem(onClick = {
                        selectedIcon = icon
                        mapViewModel.selectedIcon.value = icon.toString()
                        expanded = false
                    }) {
                        Image(
                            painter = painterResource(id = icon),
                            contentDescription = null,
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
            }
            Image(
                painter = painterResource(id = selectedIcon),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    mapViewModel.setShowState(false)

                    val latLng = mapViewModel.getPosition()
                    val photoBitmap = mapViewModel.photoTaken.value
                    if (photoBitmap != null) {
                        mapViewModel.uploadImage(photoBitmap) { imageUrl ->
                            val markerToAdd = MapMarkers(
                                id = null,
                                userId = FirebaseAuth.getInstance().currentUser?.uid,
                                position = LatLong(latLng.latitude, latLng.longitude),
                                title = title,
                                snippet = snippet,
                                icon = selectedIcon.toString(),
                                photo = imageUrl
                            )

                            mapViewModel.addMarker(markerToAdd)
                            navController.navigate(Routes.MapScreen.route)
                            mapViewModel.resetInputFields()
                        }
                    } else {
                        val markerToAdd = MapMarkers(
                            id = null,
                            userId = FirebaseAuth.getInstance().currentUser?.uid,
                            position = LatLong(latLng.latitude, latLng.longitude),
                            title = title,
                            snippet = snippet,
                            icon = selectedIcon.toString(),
                            photo = "" // Usar una cadena vacía como URL de la imagen si photoBitmap es nulo
                        )

                        mapViewModel.addMarker(markerToAdd)
                        navController.navigate(Routes.MapScreen.route)
                        mapViewModel.resetInputFields()
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xffFF914D)),
                modifier = Modifier.width(150.dp)
            ) {
                Text("GUARDAR")
            }
            Button(
                onClick = {
                    mapViewModel.resetInputFields()
                    mapViewModel.setShowState(false)
                },
                colors = ButtonDefaults.buttonColors(Color(0xffFF914D)),
                modifier = Modifier.width(150.dp)
            ){
                Text("Cancelar")

            }
            Button(
                onClick = {
                    if (!isCameraPermissionGranted) {
                        launcher.launch(Manifest.permission.CAMERA)
                    } else {
                        navController.navigate(Routes.TakePhotoScreen.route)
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xffFF914D)),
                modifier = Modifier.width(150.dp)
            ) {
                if (showPermissionDenied) {
                    PermissionDeclinedScreen()
                }
                Text("Camara")
            }
            photoTaken?.let { photo ->
                Image(
                    bitmap = photo.asImageBitmap(),
                    contentDescription = "Taken photo",
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
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