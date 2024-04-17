package com.example.mapapp.view

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import com.example.mapapp.R
import com.example.mapapp.viewmodel.MapViewModel
import javax.xml.transform.Source


@Composable
fun GalleryScreen(navController: NavController, mapViewModel: MapViewModel) {
    val context = LocalContext.current

    val img: Bitmap? = ContextCompat.getDrawable(context, R.drawable.empty_image)?.toBitmap()
    var bitmap by remember { mutableStateOf(img) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = {

        }) {
            Text("Open Gallery")
        }
        Image(
            bitmap = if (bitmap != null) bitmap!!.asImageBitmap() else img!!.asImageBitmap(), contentDescription = null,
            contentScale = ContentScale.Crop, modifier = Modifier
                .clip(CircleShape)
                .size(250.dp)
                .background(Color.Blue)
                .border(1.dp, Color.White, CircleShape)
        )
    }
}
