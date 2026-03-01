package com.example.playlist_maker.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.playlist_maker.BottomSheetExample
import com.example.playlist_maker.FloatButtonExample
import com.example.playlist_maker.ui.favorites.FavoritesView
import com.example.playlist_maker.ui.main.MainView
import com.example.playlist_maker.ui.playlists.PlaylistsView
import com.example.playlist_maker.ui.search.SearchView
import com.example.playlist_maker.ui.search.SearchViewModel
import com.example.playlist_maker.ui.settings.SettingsView


//main code
//class MainActivity : ComponentActivity() {
//    private val searchViewModel by viewModels<SearchViewModel>{
//        SearchViewModel.getViewModelFactory()
//    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            PlaylistHost(searchViewModel = searchViewModel)
//        }
//    }
//}


// BottomSheet &  FloatingButton example code
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var bottomSheetState by remember { mutableStateOf(false) }
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                FloatButtonExample(modifier = Modifier.padding(innerPadding)){
                    bottomSheetState = true
                }
            }
            BottomSheetExample(
                modifier = Modifier,
                isShowPanel = bottomSheetState,
                onDismissRequest = { bottomSheetState  = false },
                content = "Это панель BottomSheet"
            )
        }
    }
}



@Composable
fun PlaylistHost(
    searchViewModel: SearchViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainView(navController)
        }
        composable("search") {
            SearchView(viewModel = searchViewModel, navController = navController)
        }
        composable("favorites") {
            FavoritesView(navController)
        }
        composable("playlists") {
            PlaylistsView(navController)
        }
        composable("settings") {
            SettingsView(navController)
        }
    }
}

