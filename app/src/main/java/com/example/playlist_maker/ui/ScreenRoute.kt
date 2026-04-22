package com.example.playlist_maker.ui

sealed class ScreenRoute(val route: String) {
    object Main : ScreenRoute("main")
    object Search : ScreenRoute("search")
    object Favorites : ScreenRoute("favorites")
    object Playlists : ScreenRoute("playlists")
    object Settings : ScreenRoute("settings")
}