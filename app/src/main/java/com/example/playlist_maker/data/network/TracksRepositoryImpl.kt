package com.example.playlist_maker.data.network

import com.example.playlist_maker.data.DatabaseProvider
import com.example.playlist_maker.data.db.TrackEntity
import com.example.playlist_maker.data.dto.TrackDto
import com.example.playlist_maker.data.dto.TracksSearchRequest
import com.example.playlist_maker.domain.NetworkClient
import com.example.playlist_maker.domain.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TracksRepositoryImpl(
    private val networkClient: NetworkClient
) : TracksRepository {
    private val trackDao = DatabaseProvider.database.trackDao()

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        return when (response.resultCode) {
            -1 -> throw java.io.IOException("Нет соединения с интернетом")
            -2 -> throw Exception("Ошибка сервера")
            200 -> {
                val searchResponse = response as com.example.playlist_maker.data.dto.TracksSearchResponse
                searchResponse.results.map { it.toTrack() }
            }
            else -> throw Exception("Неизвестная ошибка: ${response.resultCode}")
        }
    }

    override suspend fun getAllTracks(): List<Track> {
        delay(1000)
        val entities: List<TrackEntity> = trackDao.getAllTracks().first()
        return entities.map { entity -> entity.toDomain() }
    }

    override suspend fun saveTrack(track: Track) {
        trackDao.insertTrack(track.toEntity())
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return trackDao.getTrackByNameAndArtist(track.trackName, track.artistName)
            .map { entity -> entity?.toDomain() }
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return trackDao.getFavoriteTracks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        trackDao.insertTrack(track.copy(playlistId = playlistId).toEntity())
    }

    override suspend fun deleteTrackFromPlaylist(track: Track) {
        trackDao.removeFromPlaylist(track.id)
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        trackDao.updateFavoriteStatus(track.id, isFavorite)
    }

    override fun deleteTracksByPlaylistId(playlistId: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            trackDao.removeAllFromPlaylist(playlistId)
        }
    }

    override fun getTrackById(trackId: Long): Flow<Track?> {
        return trackDao.getTrackById(trackId).map { entity -> entity?.toDomain() }
    }
}

internal fun Track.toEntity(): TrackEntity {
    return TrackEntity(
        id = id,
        trackName = trackName,
        artistName = artistName,
        trackTime = trackTime,
        image = image,
        previewUrl = previewUrl,
        isFavorite = favorite,
        playlistId = playlistId
    )
}

internal fun TrackEntity.toDomain(): Track {
    return Track(
        id = id,
        trackName = trackName,
        artistName = artistName,
        trackTime = trackTime,
        image = image,
        previewUrl = previewUrl,
        favorite = isFavorite,
        playlistId = playlistId
    )
}

private fun TrackDto.toTrack(): Track {
    return Track(
        id = id,
        trackName = trackName,
        artistName = artistName,
        trackTime = formatTrackTime(trackTimeMillis),
        image = image ?: "",
        previewUrl = previewUrl,
        favorite = false,
        playlistId = 0L
    )
}

private fun formatTrackTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}