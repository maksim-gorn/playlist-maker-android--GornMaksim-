package com.example.playlist_maker.data

import android.util.Log
import com.example.playlist_maker.data.network.Track
import com.example.playlist_maker.ui.search.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class DatabaseMock(
    private val scope: CoroutineScope,
) {
    private val historyList = mutableListOf<String>()
    private val _historyUpdates = MutableSharedFlow<Unit>()
    private val playlists = mutableListOf<Playlist>()
    private val tracks = mutableListOf<Track>()

    private val _tracksUpdates = MutableSharedFlow<Unit>()
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

    init {
        this.tracks.addAll(listTracks)
    }

    fun getHistory(): List<String> {
        return historyList.toList()
    }

    fun getAllTracks(): List<Track>
    {
        Log.d("DatabaseMock", "getAllTracks was called")
        return tracks.toList()
    }

    fun addToHistory(word: String) {
        historyList.add(word)
        notifyHistoryChanged()
    }

    private fun notifyHistoryChanged() {
        scope.launch(Dispatchers.IO) {
            _historyUpdates.emit(Unit)
        }
    }

    fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        delay(500) // Имитируем задержку загрузки из базы данных
        val filteredPlaylists = mutableListOf<Playlist>()
        playlists.forEach { playlist ->
            val playlistTracks = tracks.filter { track ->
                track.playlistId == playlist.id
            }
            filteredPlaylists.add(playlist.copy(tracks = playlistTracks))
        }

        emit(filteredPlaylists.toList())
        delay(100)
    }

    fun getPlaylist(id: Long): Flow<Playlist?> = flow {
        emit(playlists.find { it.id == id })
    }

    fun addNewPlaylist(name: String, description: String) {
        playlists.add(
            Playlist(
                id = playlists.size.toLong() + 1,
                name = name,
                description = description,
                tracks = emptyList()
            )
        )
    }

    fun deletePlaylistById(playlistId: Long) {
        playlists.removeIf { it.id == playlistId }
    }

    fun deleteTrackFromPlaylist(trackId: Long) {
        tracks.removeIf { it.id == trackId }
    }

    fun getTrackByNameAndArtist(track: Track): Flow<Track?> = flow {
        emit(tracks.find { it.trackName == track.trackName && it.artistName == track.artistName })
    }

    fun insertTrack(track: Track) {
        tracks.removeIf { it.id == track.id }
        tracks.add(track)
        notifyTracksChanged() // Добавьте эту строку
    }

    fun getFavoriteTracks(): Flow<List<Track>> = flow {
        delay(300) // Имитируем задержку
        val favorites = tracks.filter { it.favorite }
        emit(favorites)
    }

    fun deleteTracksByPlaylistId(playlistId: Long) {
        tracks.removeIf { it.playlistId == playlistId }
    }


    fun searchTracks(expression: String): List<Track> {
        var tracksToReturn = tracks.filter {it.trackName.contains(expression, true)
                || it.artistName.contains(expression, true)}
        Log.d("DatabaseMock", "searchTracks was called")
        Log.d("DatabaseMock", "returning " + tracksToReturn.size+" tracks")
        return tracksToReturn

    }

    fun getTrackById(trackId: Long): Flow<Track?> = flow {
        while (true) {
            val track = tracks.find { it.id == trackId }
            emit(track)
            // ждем обновление
            _tracksUpdates.first()
        }
    }.catch { e ->

        emit(null)
    }

    private fun notifyTracksChanged() {
        scope.launch(Dispatchers.IO) {
            _tracksUpdates.emit(Unit)
        }
    }

}

