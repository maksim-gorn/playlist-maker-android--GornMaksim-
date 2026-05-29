package com.example.playlist_maker.domain

import com.example.playlist_maker.data.network.Track
import kotlinx.coroutines.flow.Flow

//interface TracksRepository {
//    suspend fun getAllTracks(): List<Track>
//    suspend fun searchTracks(expression: String): List<Track>
//}

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>

    suspend fun getAllTracks(): List<Track>

    suspend fun saveTrack(track: Track)

    fun getTrackByNameAndArtist(track: Track): Flow<Track?>

    fun getFavoriteTracks(): Flow<List<Track>>

    suspend fun insertTrackToPlaylist(track: Track, playlistId: Long)

    suspend fun deleteTrackFromPlaylist(track: Track)

    suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean)

    fun deleteTracksByPlaylistId(playlistId: Long)

    fun getTrackById(trackId: Long): Flow<Track?>


}