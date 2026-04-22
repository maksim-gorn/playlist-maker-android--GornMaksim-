package com.example.playlist_maker.data.network

import com.example.playlist_maker.data.DatabaseMock
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
    private val database = DatabaseMock(
        scope = scope
    )

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
}


val listTracks = listOf(
    Track(
        id = 1L,
        trackName = "Владивосток 2000",
        artistName = "Мумий Троль",
        trackTime = "2:38",
        image = "",
        favorite = false,
        playlistId = 0L
    ),
    Track(
        id = 2L,
        trackName = "Группа крови",
        artistName = "Кино",
        trackTime = "4:43",
        image = "",
        favorite = false,
        playlistId = 0L
    ),
    Track(
        id = 3L,
        trackName = "Не смотри назад",
        artistName = "Ария",
        trackTime = "5:12",
        image = "",
        favorite = false,
        playlistId = 0L
    ),
    Track(
        id = 4L,
        trackName = "Звезда по имени Солнце",
        artistName = "Кино",
        trackTime = "3:45",
        image = "",
        favorite = false,
        playlistId = 0L
    ),
    Track(
        id = 5L,
        trackName = "Лондон",
        artistName = "Аквариум",
        trackTime = "4:32",
        image = "",
        favorite = false,
        playlistId = 0L
    ),
    Track(
        id = 6L,
        trackName = "На заре",
        artistName = "Альянс",
        trackTime = "3:50",
        image = "",
        favorite = false,
        playlistId = 0L
    ),
    Track(
        id = 7L,
        trackName = "Перемен",
        artistName = "Кино",
        trackTime = "4:56",
        image = "",
        favorite = false,
        playlistId = 0L
    ),
    Track(
        id = 8L,
        trackName = "Розовый фламинго",
        artistName = "Сплин",
        trackTime = "3:15",
        image = "",
        favorite = false,
        playlistId = 0L
    ),
    Track(
        id = 9L,
        trackName = "Танцевать",
        artistName = "Мельница",
        trackTime = "3:42",
        image = "",
        favorite = false,
        playlistId = 0L
    ),
    Track(
        id = 10L,
        trackName = "Чёрный бумер",
        artistName = "Серега",
        trackTime = "4:01",
        image = "",
        favorite = false,
        playlistId = 0L
    )
)