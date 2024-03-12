package com.example.mapapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mapapp.navigate.BottomNavigationScreens
import com.example.mapapp.navigate.Routes
import com.example.mapapp.ui.theme.MapAppTheme
import com.example.mapapp.view.MapScreen
import com.example.mapapp.viewModel.MapViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    val bottomNavigationItems = listOf(
        BottomNavigationScreens.Home,
        BottomNavigationScreens.Favorite
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        val mapViewModel by viewModels<MapViewModel>()


        super.onCreate(savedInstanceState)
        setContent {
            MapAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navigationController = rememberNavController()

                    MyDrawer(mapViewModel = mapViewModel, bottomNavigationItems = bottomNavigationItems)
                }
            }
        }
    }
}

@Composable
fun MyBottomAppBar(navController: NavController, bottomNavigationItems: List<BottomNavigationScreens>) {

    BottomNavigation(backgroundColor = Color.Red) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        bottomNavigationItems.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                    )
                },

                label = { androidx.compose.material.Text("") },
                selected = currentRoute == item.route,
                selectedContentColor = Color.Black,
                unselectedContentColor = Color(0xFF1b4445),
                alwaysShowLabel = false,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route)
                    }
                }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(navController: NavController, mapViewModel: MapViewModel, state: DrawerState, scope: CoroutineScope) {
    val showSearchBar: Boolean by mapViewModel.show.observeAsState(false)



    TopAppBar(
        title = { Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Marcadores"
        ) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.Red,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        navigationIcon = {openBurgerMenu(state = state , scope = scope)} ,
        actions = {

            IconButton(onClick = { mapViewModel.changeShow() }) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
            }
            if (showSearchBar) {
                MySearchBar(mapViewModel)

            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(mapViewModel: MapViewModel) {
    val searchText by mapViewModel.searchText.observeAsState()
    searchText?.let {
        SearchBar(
            query = it,
            onQueryChange = { mapViewModel.onSearchTextChange(it) },
            onSearch = { mapViewModel.onSearchTextChange(it) },
            active = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            },
            placeholder = { Text("Introduce para buscar") },
            onActiveChange = {},
            modifier = Modifier
                .fillMaxHeight(0.1f)
                .clip(RectangleShape)
        ) {

        }
    }
}

@Composable
fun ScaffoldMenu(
    navigationController: NavHostController,
    mapViewModel: MapViewModel,
    state: DrawerState,
    scope: CoroutineScope,
    bottomNavigationItems: List<BottomNavigationScreens>
) {
    Scaffold(
        bottomBar = { MyBottomAppBar(navigationController, bottomNavigationItems) },
        topBar = { MyTopAppBar(navigationController, mapViewModel, state, scope) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            NavHost(
                navController = navigationController,
                startDestination = Routes.MapScreen.route
            ) {
                composable(Routes.MapScreen.route) {
                    MapScreen(navigationController)
                }
            }
        }
    }
}

@Composable
fun MyDrawer(mapViewModel: MapViewModel, bottomNavigationItems: List<BottomNavigationScreens>) {
    val navigationController = rememberNavController()
    val scope = rememberCoroutineScope()
    val state: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(drawerState = state, modifier = Modifier.clickable { scope.launch { state.close() } },
        gesturesEnabled = state.isOpen, drawerContent = {
        ModalDrawerSheet {

            Icon(imageVector = Icons.Filled.Menu, contentDescription = "Search")
            Text("Menu", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineLarge)
            Divider()
            NavigationDrawerItem(label = { Text(text = "Map") },
                selected = false,
                onClick = {
                    scope.launch {
                        state.close()
                    }
                    navigationController.navigate(Routes.MapScreen.route)

                })
            Divider()
            NavigationDrawerItem(label = { Text(text = "Marker List") },
                selected = false,
                onClick = {
                    scope.launch {
                        state.close()
                    }
                    navigationController.navigate(Routes.MarksScreen.route)
                })
            Divider()
            NavigationDrawerItem(label = { Text(text =  "Add Marker") },
                selected = false,
                onClick = {
                    scope.launch {
                        state.close()
                    }
                    navigationController.navigate(Routes.MarksScreen.route)
                })

        }
    }) {
        ScaffoldMenu(navigationController, mapViewModel, state, scope, bottomNavigationItems)
    }
}
@Composable
private fun openBurgerMenu(state: DrawerState, scope: CoroutineScope) {
    IconButton(onClick =
    {
        scope.launch {
            state.open()
        }
    }
    ) {
        Icon(imageVector = Icons.Filled.Menu, contentDescription = "BurgerMenu")
    }
}

