package com.example.playlist_maker.data

import com.example.playlist_maker.data.network.Track

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    var tracks: List<Track>
)