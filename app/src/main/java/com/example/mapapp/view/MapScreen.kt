package com.example.mapapp.view
//cameraX

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ComponentActivity
import androidx.navigation.NavController
import com.example.mapapp.R
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun MapScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        var isCameraOpen by remember { mutableStateOf(false) }
        val itb = LatLng(41.4534265, 2.1837151)
        val cameraPositionState = rememberCameraPositionState{
            position = CameraPosition.fromLatLngZoom(itb, 10f)
        }
        GoogleMap(

            modifier = Modifier
                .fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = itb),
                title = "ITB",
                snippet = "Marker ai ITB"
            )

        }
        Button(
            onClick = { isCameraOpen = true },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Open Camera")
        }

        // Open CameraCapture composable if isCameraOpen is true
        if (isCameraOpen) {
            CameraCapture { capturedBitmap ->
                // Handle the captured photo (e.g., save, process, etc.)
                isCameraOpen = false
            }
        }
    }
}

@Composable
fun CameraCapture(onPhotoCaptured: (Bitmap) -> Unit) {
    var cameraExecutor: ExecutorService by remember { mutableStateOf(Executors.newSingleThreadExecutor()) }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var isCapturing by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val density = LocalDensity.current.density
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    DisposableEffect(context) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build()

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        preview.setSurfaceProvider(PreviewView(context).createSurfaceProvider())

        cameraProvider.bindToLifecycle(
            (context as ComponentActivity),
            cameraSelector,
            preview
        )

        onDispose {
            cameraExecutor.shutdown()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Captured Image",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            )

            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap!!,
                    contentDescription = "Captured Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                )
            }

            Button(
                onClick = {
                    if (!isCapturing) {
                        isCapturing = true
                        takePhoto(context, cameraExecutor) { bitmap ->
                            imageBitmap = bitmap
                            onPhotoCaptured(bitmap.asAndroidBitmap())
                            isCapturing = false
                        }
                    }
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Camera, contentDescription = null)
                Text("Capture Photo")
            }
        }
    }
}

fun ImageBitmap.asAndroidBitmap(): Bitmap {
    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
        copyPixelsFromBuffer(this@asAndroidBitmap.buffer.asReadOnlyBuffer())
    }
}

private fun takePhoto(context: Context, cameraExecutor: ExecutorService, onPhotoCaptured: (Bitmap) -> Unit) {
    // Implement CameraX logic here to capture photos
    // This example captures a blank bitmap for demonstration purposes
    val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)

    onPhotoCaptured(bitmap)
}