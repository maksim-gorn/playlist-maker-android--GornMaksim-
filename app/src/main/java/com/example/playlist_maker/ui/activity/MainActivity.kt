package com.example.playlist_maker.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.playlist_maker.data.network.RetrofitNetworkClient
import com.example.playlist_maker.data.network.Track
import com.example.playlist_maker.data.network.TracksRepositoryImpl
import com.example.playlist_maker.domain.TracksRepository
import com.example.playlist_maker.ui.activity.ScreenRoute
import com.example.playlist_maker.ui.favorites.FavoritesView
import com.example.playlist_maker.ui.main.MainView
import com.example.playlist_maker.ui.playlists.CreatePlaylistScreen
import com.example.playlist_maker.ui.playlists.PlaylistDetailsScreen
import com.example.playlist_maker.ui.playlists.PlaylistsView
import com.example.playlist_maker.ui.playlists.PlaylistsViewModel
import com.example.playlist_maker.ui.search.SearchView
//import com.example.playlist_maker.ui.search.SearchView
import com.example.playlist_maker.ui.search.SearchViewModel
import com.example.playlist_maker.ui.settings.SettingsView
import com.example.playlist_maker.ui.track.TrackDetailsScreen
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val searchViewModel: SearchViewModel by viewModels ()
    private val playlistsViewModel: PlaylistsViewModel by viewModels()

    //создать как синглтон
    private val tracksRepository: TracksRepository by lazy {
        val networkClient = RetrofitNetworkClient.create()
        TracksRepositoryImpl(networkClient = networkClient)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppHost(
                searchViewModel = searchViewModel,
                playlistsViewModel = playlistsViewModel,
                tracksRepository = tracksRepository)
        }
    }
}



@Composable
fun AppHost(
    searchViewModel: SearchViewModel,
    playlistsViewModel: PlaylistsViewModel,
    tracksRepository: TracksRepository
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = ScreenRoute.Main.route  // Используем enum теперь
    ) {
        composable(ScreenRoute.Main.route) {
            MainView(navController)
        }
        composable(ScreenRoute.Search.route) {
            SearchView(
                modifier = Modifier.padding(all = 1.dp),
                searchViewModel = searchViewModel,
                onClick = { track ->
                    //сохраняем трек в локальную БД перед открытием деталей
                    scope.launch {
                        tracksRepository.saveTrack(track)
                        navController.navigate("track/${track.id}")
                    }
                },
                navController = navController
            )
        }
        composable(ScreenRoute.Favorites.route) {
            FavoritesView(
                navController = navController,
                tracksRepository = tracksRepository
            )
        }
        composable(ScreenRoute.Playlists.route) {
            PlaylistsView(
                modifier = Modifier,
                playlistsViewModel = playlistsViewModel,
                navController = navController,
                addNewPlaylist = {navController.navigate(ScreenRoute.CreatePlaylistScreen.route)},
                onPlaylistClick = { playlistId ->
                    navController.navigate(ScreenRoute.PlaylistDetails.createRoute(playlistId))
                }
            )
        }

        composable(ScreenRoute.CreatePlaylistScreen.route) {
            CreatePlaylistScreen(
                modifier = Modifier,
                playlistsViewModel = playlistsViewModel,
                navController = navController
            )
        }

        composable(ScreenRoute.Settings.route) {
            SettingsView(navController)
        }

        composable(
            route = "playlist/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong("id") ?: return@composable

            PlaylistDetailsScreen(
                playlistId = playlistId,
                playlistsViewModel = playlistsViewModel,
                onBackClick = { navController.popBackStack() },
                onTrackClick = { track ->
                    scope.launch {
                        tracksRepository.saveTrack(track)
                        navController.navigate("track/${track.id}")
                    }
                }
            )
        }

        composable(
            route = "track/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val trackId = backStackEntry.arguments?.getLong("id") ?: return@composable

            TrackDetailsScreen(
                trackId = trackId,
                onBackClick = { navController.popBackStack() },
                tracksRepository = tracksRepository,
                playlistsViewModel = playlistsViewModel
            )
        }

    }
}
