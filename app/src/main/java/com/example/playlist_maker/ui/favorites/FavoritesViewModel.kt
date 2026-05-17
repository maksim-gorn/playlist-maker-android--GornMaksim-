package com.example.playlist_maker.ui.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.data.network.Track
import com.example.playlist_maker.domain.TracksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val tracksRepository: TracksRepository
) : ViewModel() {

    private val _favoriteTracksState = MutableStateFlow<List<Track>>(emptyList())
    val favoriteTracksState: StateFlow<List<Track>> = _favoriteTracksState.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadFavoriteTracks()
    }

    private fun loadFavoriteTracks() {
        viewModelScope.launch {
            tracksRepository.getFavoriteTracks()
                .catch { e ->
                    Log.e("FavoritesVM", "Error loading favorites", e)
                    _isLoading.value = false
                    emit(emptyList())
                }
                .collect { tracks ->
                    _favoriteTracksState.value = tracks
                    _isLoading.value = false
                }
        }
    }

    fun toggleFavorite(track: Track) {
        viewModelScope.launch {
            tracksRepository.updateTrackFavoriteStatus(
                track = track,
                isFavorite = !track.favorite
            )
        }
    }

    fun removeFromFavorites(track: Track) {
        viewModelScope.launch {
            tracksRepository.updateTrackFavoriteStatus(
                track = track,
                isFavorite = false
            )
        }
    }
}