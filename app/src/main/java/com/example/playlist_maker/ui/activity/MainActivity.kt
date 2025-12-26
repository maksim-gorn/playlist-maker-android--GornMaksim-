package com.example.playlist_maker.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.playlist_maker.ui.favorites.FavoritesView
import com.example.playlist_maker.ui.main.MainView
import com.example.playlist_maker.ui.playlists.PlaylistsView
import com.example.playlist_maker.ui.search.SearchView
import com.example.playlist_maker.ui.search.SearchViewModel
import com.example.playlist_maker.ui.settings.SettingsView

class MainActivity : ComponentActivity() {
    private val searchViewModel by viewModels<SearchViewModel>{
        SearchViewModel.getViewModelFactory()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlaylistHost(searchViewModel = searchViewModel)
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