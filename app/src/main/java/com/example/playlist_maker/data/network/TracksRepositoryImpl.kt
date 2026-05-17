package com.example.playlist_maker.data.network

import com.example.playlist_maker.data.DatabaseProvider
import com.example.playlist_maker.data.dto.TracksSearchRequest
import com.example.playlist_maker.data.dto.TracksSearchResponse
import com.example.playlist_maker.domain.NetworkClient
import com.example.playlist_maker.domain.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class TracksRepositoryImpl(
    private val scope: CoroutineScope
) : TracksRepository {
    private val database = DatabaseProvider.database

    override suspend fun searchTracks(expression: String): List<Track> {
        return database.searchTracks(expression)
    }

    override suspend fun getAllTracks(): List<Track> {
        delay(1000)// Имитируем запрос к серверу
        return database.getAllTracks()
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return database.getTrackByNameAndArtist(track)
    }

    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        database.insertTrack(track.copy(playlistId = playlistId))
    }

    override suspend fun deleteTrackFromPlaylist(track: Track) {
        database.insertTrack(track.copy(playlistId = 0))
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        database.insertTrack(track.copy(favorite = isFavorite))
    }

    override fun deleteTracksByPlaylistId(playlistId: Long) {
        database.deleteTracksByPlaylistId(playlistId)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return database.getFavoriteTracks()
    }

    override fun getTrackById(trackId: Long): Flow<Track?> {
        return database.getTrackById(trackId)
    }
}
