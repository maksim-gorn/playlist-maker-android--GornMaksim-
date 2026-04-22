package com.example.playlist_maker.ui.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.data.DatabaseMock
import com.example.playlist_maker.data.Playlist
import com.example.playlist_maker.domain.PlaylistsRepository
import com.example.playlist_maker.data.network.PlaylistsRepositoryImpl
import com.example.playlist_maker.data.network.Track
import com.example.playlist_maker.data.network.TracksRepositoryImpl
import com.example.playlist_maker.domain.TracksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class PlaylistsViewModel() : ViewModel() {
    private val playlistsRepository: PlaylistsRepository =
        PlaylistsRepositoryImpl(scope = viewModelScope)
    private val tracksRepository: TracksRepository =
        TracksRepositoryImpl(scope = viewModelScope)
    // Используем мок базы вместо репозитория
    private val databaseRepository: DatabaseMock = DatabaseMock(scope = viewModelScope)

    init {
        this.createNewPlayList("Тестовый 1", "Тестовый плейлист 1 для проверки отображения")
        this.createNewPlayList("Тестовый 2", "Тестовый плейлист 2 для проверки отображения")
        this.createNewPlayList("Тестовый 3", "Тестовый плейлист 3 для проверки отображения")
    }

    val playlists: Flow<List<Playlist>> = flow {
        val collectedPlaylists = mutableListOf<Playlist>()
        playlistsRepository.getAllPlaylists().collect { playlist ->
            collectedPlaylists.addAll(playlist)
            emit(collectedPlaylists.toList())
        }
    }
    val favoriteList: Flow<List<Track>> = databaseRepository.getFavoriteTracks()

    fun createNewPlayList(namePlaylist: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsRepository.addNewPlaylist(namePlaylist, description)
        }
    }

    suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        tracksRepository.insertTrackToPlaylist(track, playlistId)
    }

    suspend fun toggleFavorite(track: Track, isFavorite: Boolean) {
        tracksRepository.updateTrackFavoriteStatus(track, isFavorite)
    }

    suspend fun deleteTrackFromPlaylist(track: Track) {
        tracksRepository.deleteTrackFromPlaylist(track)
    }

    suspend fun deletePlaylistById(id: Long) {
        tracksRepository.deleteTracksByPlaylistId(id)
        playlistsRepository.deletePlaylistById(id)
    }

    suspend fun isExist(track: Track): Track? {
        return tracksRepository.getTrackByNameAndArtist(track = track).firstOrNull()
    }
}