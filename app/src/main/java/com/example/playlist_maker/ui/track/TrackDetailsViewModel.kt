package com.example.playlist_maker.ui.track

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.data.network.Track
import com.example.playlist_maker.data.network.TracksRepositoryImpl
import com.example.playlist_maker.domain.TracksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TrackDetailsViewModel(
    private val trackId: Long,
    private val tracksRepository: TracksRepository
) : ViewModel() {

    private val _trackState = MutableStateFlow<Track?>(null)
    val trackState: StateFlow<Track?> = _trackState.asStateFlow()

    init {
        loadTrack()
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

    fun toggleFavorite() {
        val track = _trackState.value ?: return
        viewModelScope.launch {
            tracksRepository.updateTrackFavoriteStatus(
                track = track,
                isFavorite = !track.favorite
            )
            // Не нужно обновлять _trackState вручную,
            // Flow из репозитория обновит автоматически
        }
    }

    fun addToPlaylist(playlistId: Long) {
        val track = _trackState.value ?: return
        viewModelScope.launch {
            tracksRepository.insertTrackToPlaylist(
                track = track,
                playlistId = playlistId
            )
        }
    }

    fun removeFromPlaylist() {
        val track = _trackState.value ?: return
        viewModelScope.launch {
            tracksRepository.deleteTrackFromPlaylist(track)
        }
    }
}