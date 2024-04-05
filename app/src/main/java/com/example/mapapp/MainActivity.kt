package com.example.mapapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapapp.navigate.Routes
import com.example.mapapp.view.GalleryScreen
import com.example.mapapp.view.List
import com.example.mapapp.view.MapScreen
import com.example.mapapp.view.DetailScreen
import com.example.mapapp.view.TakePhotoScreen
import com.example.mapapp.viewmodel.MapViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mapViewModel by viewModels<MapViewModel>()
        setContent {
            val navigationController = rememberNavController()

            val permissionState =
                rememberPermissionState(permission = android.Manifest.permission.ACCESS_FINE_LOCATION)
            LaunchedEffect(Unit){
                permissionState.launchPermissionRequest()
            }
            if (permissionState.status.isGranted){
                MyDrawer(navigationController, mapViewModel)
            }
            else{ Text(text = "Need permision")}
        }
    }
}

val screensFromDrawer = listOf(
    Routes.MapScreen,
    Routes.List,
    Routes.DetailScreen
)

@Composable
fun MyDrawer(
    navController: NavController,
    mapViewModel: MapViewModel
) {
    val scope = rememberCoroutineScope()
    val state: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = state,
        gesturesEnabled = false,
        modifier = Modifier.clickable { scope.launch { state.close() } },
        drawerContent = {
            ModalDrawerSheet {
                IconButton(
                    onClick = {
                        scope.launch {
                            state.close()
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "",
                        tint = Color(0xffFF914D)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    screensFromDrawer.forEach { screen ->
                        Button(
                            colors = ButtonDefaults.buttonColors(Color(0xffFF914D)),
                            modifier = Modifier
                                .width(160.dp),
                            shape = CircleShape,
                            onClick = {
                                navController.navigate(screen.route)
                                scope.launch { state.close() }
                            }
                        ) {
                        }
                    }
                }
            }
        }) {
        MyScaffold(
            mapViewModel,
            state,
            navController
        )
    }
}


@Composable
fun MyScaffold(
    mapViewModel: MapViewModel,
    state: DrawerState,
    navController: NavController
) {
    Scaffold(
        topBar = { MyTopAppBar(mapViewModel, state) },
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color(0xffFF914D))
        ) {
            Box(Modifier.padding(it)) {
                NavHost(
                    navController = navController as NavHostController,
                    startDestination = Routes.MapScreen.route
                ) {
                    composable(Routes.MapScreen.route) {
                        MapScreen(navController, mapViewModel)
                    }
                    composable(Routes.List.route) {
                        List(navController, mapViewModel)
                    }
                    composable(Routes.DetailScreen.route) {
                        DetailScreen(navController, mapViewModel)
                    }
                    composable(Routes.TakePhotoScreen.route) {
                        TakePhotoScreen(navController, mapViewModel)
                    }
                    composable(Routes.GalleryScreen.route) {
                        GalleryScreen(navController, mapViewModel)
                    }

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(mapViewModel: MapViewModel, state: DrawerState) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(20.dp))

            }
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    state.open()
                }
            }) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color(0xffFF914D)
                )
            }
        },
    )
}