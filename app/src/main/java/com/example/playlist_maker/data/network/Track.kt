package com.example.playlist_maker.data.network

data class Track(
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val image: String,
    val previewUrl: String? = null,
    var favorite: Boolean,
    var playlistId: Long
)