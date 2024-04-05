package com.example.mapapp.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.example.mapapp.MyDrawer
import com.example.mapapp.viewmodel.MapViewModel
import com.example.mapapp.viewmodel.Marker


@Composable
fun List(navController: NavController, mapViewModel: MapViewModel) {
    val lazyColumnState = rememberLazyListState()
    val marcadores: List<Marker> by mapViewModel.markers.observeAsState(emptyList())
    val searchTextState = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffFF914D))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            MySearchBar(navController,mapViewModel)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                state = lazyColumnState
            ) {
                items(marcadores) { marker ->
                    if (marker.title.contains(searchTextState.value.text, ignoreCase = true)) {
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
    marker: Marker,
    navController: NavController,
    mapViewModel: MapViewModel
) {
    Card(
        border = BorderStroke(2.dp, Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = marker.title,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            IconButton(
                onClick = { mapViewModel.removeMarker(marker) },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(Icons.Default.Clear, contentDescription = "")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(
    navController: NavController,
    mapViewModel: MapViewModel
) {
    val searchedMarkers by mapViewModel.searchedMarkers.observeAsState(emptyList())
    val searchText = searchedMarkers.joinToString(separator = "\n") { it.title }

    SearchBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        query = searchText,
        onQueryChange = { mapViewModel.onSearchTextChange(it) },
        onSearch = { mapViewModel.onSearchTextChange(it) },
        active = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "",
                tint = Color(0xFFFFFFFF)
            )
        },
        placeholder = {
            Text(
                "Buscar marcadores...",
                style = TextStyle(fontSize = 20.sp),
                color = Color(0x81DEDCE0)
            )
        },
        onActiveChange = {},
        colors = SearchBarDefaults.colors(
            containerColor = Color(0xFFFFFFFF),
            inputFieldColors = TextFieldDefaults.textFieldColors(
                focusedTextColor = Color(0xFFFFFFFF),
            )
        ),
    ) {}
}