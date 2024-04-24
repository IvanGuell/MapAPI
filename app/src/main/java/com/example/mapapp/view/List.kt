package com.example.mapapp.view

import MapMarkers
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

import com.example.mapapp.MyDrawer
import com.example.mapapp.viewmodel.MapViewModel




@Composable
fun List(navController: NavController, mapViewModel: MapViewModel) {
    val lazyColumnState = rememberLazyListState()
    val marcadores: List<MapMarkers> by mapViewModel.markerList.observeAsState(emptyList())
    val searchTextState = remember { mutableStateOf(TextFieldValue()) }
    val selectedIcon by mapViewModel.selectedIcon.observeAsState()

    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xffFF914D))
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                if (selectedIcon != null && selectedIcon!!.isNotEmpty()) {
                    Image(
                        painter = painterResource(id = selectedIcon!!.toInt()),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable { expanded = true }
                            .padding(16.dp)
                            .size(60.dp)
                    )
                } else {
                    Text(
                        text = "Select Icon",
                        modifier = Modifier
                            .clickable { expanded = true }
                            .padding(16.dp)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    mapViewModel.markerIcons.forEach { icon ->
                        DropdownMenuItem(onClick = {
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
            }
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(state = lazyColumnState) {
                items(marcadores) { marker ->
                    if (marker.title.contains(searchTextState.value.text, ignoreCase = true) &&
                        (selectedIcon == null || selectedIcon!!.isEmpty() || marker.icon.toString() == selectedIcon)) {
                        item(
                            marker,
                            navController,
                            mapViewModel
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun filtredItem(){

}

@Composable
fun item(
    marker: MapMarkers,
    navController: NavController,
    mapViewModel: MapViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp) // Ajusta la altura del Card
            .padding(bottom = 8.dp) // Agrega espacio debajo de cada Card
            .clickable {
                navController.navigate("detail_screen/${marker.title}")
            },
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(16.dp) // Cambia a forma rectangular con bordes redondeados
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = marker.title,
                fontSize = 24.sp, // Aumenta el tamaño del texto
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            AsyncImage(
                model = marker.photo,
                contentDescription = "Marker Photo",
                modifier = Modifier.size(64.dp) // Aumenta el tamaño de la foto
            )
            Image(
                painter = painterResource(id = marker.icon!!.toInt()),
                contentDescription = "Marker Icon",
                modifier = Modifier.size(64.dp) // Aumenta el tamaño del icono
            )
            IconButton(onClick = { mapViewModel.removeMarker(marker) }) {
                Icon(Icons.Filled.Clear, contentDescription = "Eliminar marcador")
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MySearchBar(
//    navController: NavController,
//    mapViewModel: MapViewModel
//) {
//    val searchedMarkers by mapViewModel.searchedMarkers.observeAsState(emptyList())
//    val searchText = searchedMarkers.joinToString(separator = "\n") { it.title }
//
//    SearchBar(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(70.dp),
//        query = searchText,
//        onQueryChange = { mapViewModel.onSearchTextChange(it) },
//        onSearch = { mapViewModel.onSearchTextChange(it) },
//        active = true,
//        leadingIcon = {
//            Icon(
//                imageVector = Icons.Filled.Search,
//                contentDescription = "",
//                tint = Color(0xFFFFFFFF)
//            )
//        },
//        placeholder = {
//            Text(
//                "Buscar marcadores...",
//                style = TextStyle(fontSize = 20.sp),
//                color = Color(0x81DEDCE0)
//            )
//        },
//        onActiveChange = {},
//        colors = SearchBarDefaults.colors(
//            containerColor = Color(0xFFFFFFFF),
//            inputFieldColors = TextFieldDefaults.textFieldColors(
//                focusedTextColor = Color(0xFFFFFFFF),
//            )
//        ),
//    ) {}
//}