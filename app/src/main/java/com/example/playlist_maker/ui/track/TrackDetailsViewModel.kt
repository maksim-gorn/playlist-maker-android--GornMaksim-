package com.example.playlist_maker.ui.track

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.data.Playlist
import com.example.playlist_maker.data.network.Track
import com.example.playlist_maker.data.network.TracksRepositoryImpl
import com.example.playlist_maker.domain.PlaylistsRepository
import com.example.playlist_maker.domain.TracksRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
//
//class TrackDetailsViewModel(
//    private val trackId: Long,
//    private val tracksRepository: TracksRepository
//) : ViewModel() {
//
//    private val _trackState = MutableStateFlow<Track?>(null)
//    val trackState: StateFlow<Track?> = _trackState.asStateFlow()
//
//    init {
//        loadTrack()
//    }
//
//    private fun loadTrack() {
//        viewModelScope.launch {
//            tracksRepository.getTrackById(trackId)
//                .catch { e ->
//                    Log.e("TrackDetailsVM", "Error loading track", e)
//                    emit(null)
//                }
//                .collect { track ->
//                    _trackState.value = track
//                }
//        }
//    }
//
//    fun toggleFavorite() {
//        val track = _trackState.value ?: return
//        viewModelScope.launch {
//            tracksRepository.updateTrackFavoriteStatus(
//                track = track,
//                isFavorite = !track.favorite
//            )
//            // Не нужно обновлять _trackState вручную,
//            // Flow из репозитория обновит автоматически
//        }
//    }
//
//    fun addToPlaylist(playlistId: Long) {
//        val track = _trackState.value ?: return
//        viewModelScope.launch {
//            tracksRepository.insertTrackToPlaylist(
//                track = track,
//                playlistId = playlistId
//            )
//        }
//    }
//
//    fun removeFromPlaylist() {
//        val track = _trackState.value ?: return
//        viewModelScope.launch {
//            tracksRepository.deleteTrackFromPlaylist(track)
//        }
//    }
//}

class TrackDetailsViewModel(
    private val trackId: Long,
    private val tracksRepository: TracksRepository,
    private val playlistsRepository: PlaylistsRepository
) : ViewModel() {

    private val _trackState = MutableStateFlow<Track?>(null)
    val trackState: StateFlow<Track?> = _trackState.asStateFlow()

    private val _playlistsState = MutableStateFlow<List<Playlist>>(emptyList())
    val playlistsState: StateFlow<List<Playlist>> = _playlistsState.asStateFlow()

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    init {
        loadTrack()
        loadPlaylists()
    }

    private fun loadTrack() {
        viewModelScope.launch {
            tracksRepository.getTrackById(trackId)
                .catch { e ->
                    Log.e("TrackDetailsVM", "Error loading track", e)
                    emit(null)
                }
                .collect { track ->
                    _trackState.value = track
                }
        }
    }

    private fun loadPlaylists() {
        viewModelScope.launch {
            playlistsRepository.getAllPlaylists()
                .catch { e ->
                    Log.e("TrackDetailsVM", "Error loading playlists", e)
                    emit(emptyList())
                }
                .collect { playlists ->
                    _playlistsState.value = playlists
                }
        }
    }

    fun toggleFavorite() {
        val track = _trackState.value ?: return
        viewModelScope.launch {
            tracksRepository.updateTrackFavoriteStatus(
                track = track,
                isFavorite = !track.favorite
            )
        }
    }

    fun showPlaylistSelector() {
        _showBottomSheet.value = true
    }

    fun hidePlaylistSelector() {
        _showBottomSheet.value = false
    }

    fun addToPlaylist(playlistId: Long) {
        val track = _trackState.value ?: return
        viewModelScope.launch {
            try {
                tracksRepository.insertTrackToPlaylist(
                    track = track,
                    playlistId = playlistId
                )
                _toastMessage.emit("Трек добавлен в плейлист")
                hidePlaylistSelector()
            } catch (e: Exception) {
                _toastMessage.emit("Ошибка при добавлении в плейлист")
                Log.e("TrackDetailsVM", "Error adding to playlist", e)
            }
        }
    }

    fun removeFromPlaylist() {
        val track = _trackState.value ?: return
        viewModelScope.launch {
            tracksRepository.deleteTrackFromPlaylist(track)
        }
    }
}