package com.example.playlist_maker.ui.activity

sealed class ScreenRoute(val route: String) {
    object Main : ScreenRoute("main")
    object Search : ScreenRoute("search")
    object Favorites : ScreenRoute("favorites")
    object Playlists : ScreenRoute("playlists")
    object CreatePlaylistScreen: ScreenRoute("createplaylist")
    object Settings : ScreenRoute("settings")
    object Track : ScreenRoute("track")
    object PlaylistDetails : ScreenRoute("playlist/{id}") {
        fun createRoute(id: Long) = "playlist/$id"
    }
}