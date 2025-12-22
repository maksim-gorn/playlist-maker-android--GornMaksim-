package com.example.playlist_maker.domain

import com.example.playlist_maker.data.network.Track

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
}