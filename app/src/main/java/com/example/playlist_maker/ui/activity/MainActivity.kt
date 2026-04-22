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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.playlist_maker.ui.ScreenRoute
import com.example.playlist_maker.ui.favorites.FavoritesView
import com.example.playlist_maker.ui.main.MainView
import com.example.playlist_maker.ui.playlists.PlaylistsView
import com.example.playlist_maker.ui.playlists.PlaylistsViewModel
import com.example.playlist_maker.ui.search.SearchView
//import com.example.playlist_maker.ui.search.SearchView
import com.example.playlist_maker.ui.search.SearchViewModel
import com.example.playlist_maker.ui.settings.SettingsView


class MainActivity : ComponentActivity() {
    private val searchViewModel: SearchViewModel by viewModels ()
    private val playlistsViewModel: PlaylistsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppHost(
                searchViewModel = searchViewModel,
                playlistsViewModel = playlistsViewModel)
        }
    }
}



@Composable
fun AppHost(
    searchViewModel: SearchViewModel,
    playlistsViewModel: PlaylistsViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ScreenRoute.Main.route  // Используем enum
    ) {
        composable(ScreenRoute.Main.route) {
            MainView(navController)
        }
        composable(ScreenRoute.Search.route) {
            SearchView(
                modifier = Modifier.padding(all = 1.dp),
                searchViewModel = searchViewModel,
                onClick = {},
                navController = navController
            )
        }
        composable(ScreenRoute.Favorites.route) {
            FavoritesView(navController)
        }
        composable(ScreenRoute.Playlists.route) {
            PlaylistsView(
                modifier = Modifier,
                playlistsViewModel = playlistsViewModel,
                navController = navController
            )
        }
        composable(ScreenRoute.Settings.route) {
            SettingsView(navController)
        }
    }
}
