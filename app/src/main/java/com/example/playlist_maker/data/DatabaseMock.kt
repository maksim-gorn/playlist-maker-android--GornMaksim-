package com.example.playlist_maker.data

import android.util.Log
import com.example.playlist_maker.data.network.Track
import com.example.playlist_maker.ui.search.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

object DatabaseProvider {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val database: DatabaseMock by lazy {
        DatabaseMock(scope = scope)
    }
}

class DatabaseMock(
    private val scope: CoroutineScope,
) {
    private val historyList = mutableListOf<String>()
    private val _historyUpdates = MutableSharedFlow<Unit>()

    // Используем StateFlow для автоматического обновления
    private val _playlistsState = MutableStateFlow<List<Playlist>>(emptyList())
    private val _tracksState = MutableStateFlow<List<Track>>(emptyList())

    private val _tracksUpdates = MutableSharedFlow<Unit>()

    // Используем изменяемый список для плейлистов
    private val playlists = mutableListOf<Playlist>()

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
        _tracksState.value = listTracks
        updatePlaylistsState()
        Log.d("DatabaseMock", "DatabaseMock initialized with ${_tracksState.value.size} tracks")
    }

    private fun updatePlaylistsState() {
        val currentPlaylists = playlists.toList()
        Log.d("DatabaseMock", "updatePlaylistsState called, playlists size: ${currentPlaylists.size}")

        val updatedPlaylists = currentPlaylists.map { playlist ->
            val playlistTracks = _tracksState.value.filter { track ->
                track.playlistId == playlist.id
            }
            Log.d("DatabaseMock", "Playlist ${playlist.name} (id: ${playlist.id}) has ${playlistTracks.size} tracks")
            playlist.copy(tracks = playlistTracks)
        }
        _playlistsState.value = updatedPlaylists
        Log.d("DatabaseMock", "Playlists updated. Total playlists: ${updatedPlaylists.size}")
    }

    fun getHistory(): List<String> {
        return historyList.toList()
    }

    fun getAllTracks(): List<Track> {
        Log.d("DatabaseMock", "getAllTracks was called")
        return _tracksState.value
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

    fun getAllPlaylists(): Flow<List<Playlist>> {
        return _playlistsState.asStateFlow()
    }

    fun getPlaylist(id: Long): Flow<Playlist?> {
        return _playlistsState.map { playlistsList ->
            playlistsList.find { it.id == id }
        }
    }

    fun addNewPlaylist(name: String, description: String) {
        val newId = if (playlists.isEmpty()) 1L else (playlists.maxOfOrNull { it.id } ?: 0L) + 1
        val newPlaylist = Playlist(
            id = newId,
            name = name,
            description = description,
            tracks = emptyList()
        )
        playlists.add(newPlaylist)
        Log.d("DatabaseMock", "New playlist added: $name with id: $newId")
        updatePlaylistsState()
    }

    fun deletePlaylistById(playlistId: Long) {
        playlists.removeIf { it.id == playlistId }
        Log.d("DatabaseMock", "Playlist deleted: $playlistId")

        // Сбрасываем playlistId у треков из удаленного плейлиста
        val currentTracks = _tracksState.value.toMutableList()
        val updatedTracks = currentTracks.map { track ->
            if (track.playlistId == playlistId) {
                track.copy(playlistId = 0L)
            } else {
                track
            }
        }
        _tracksState.value = updatedTracks
        updatePlaylistsState()
        notifyTracksChanged()
    }

    fun deleteTrackFromPlaylist(trackId: Long) {
        val currentTracks = _tracksState.value.toMutableList()
        val index = currentTracks.indexOfFirst { it.id == trackId }
        if (index != -1) {
            currentTracks[index] = currentTracks[index].copy(playlistId = 0L)
            _tracksState.value = currentTracks
            updatePlaylistsState()
            notifyTracksChanged()
            Log.d("DatabaseMock", "Track $trackId removed from playlist")
        }
    }

    fun getTrackByNameAndArtist(track: Track): Flow<Track?> = flow {
        emit(_tracksState.value.find {
            it.trackName == track.trackName && it.artistName == track.artistName
        })
    }

    fun insertTrack(track: Track) {
        val currentTracks = _tracksState.value.toMutableList()
        val index = currentTracks.indexOfFirst { it.id == track.id }

        if (index != -1) {
            currentTracks[index] = track
            Log.d("DatabaseMock", "Updating existing track: ${track.trackName}, playlistId: ${track.playlistId}")
        } else {
            currentTracks.add(track)
            Log.d("DatabaseMock", "Adding new track: ${track.trackName}, playlistId: ${track.playlistId}")
        }

        _tracksState.value = currentTracks
        updatePlaylistsState()
        notifyTracksChanged()

        // Проверяем, существует ли плейлист
        val playlistExists = playlists.any { it.id == track.playlistId }
        if (playlistExists) {
            Log.d("DatabaseMock", "Track added to existing playlist")
        } else {
            Log.d("DatabaseMock", "WARNING: Playlist with id ${track.playlistId} not found!")
        }
    }

    fun getFavoriteTracks(): Flow<List<Track>> = flow {
        delay(300)
        val favorites = _tracksState.value.filter { it.favorite }
        emit(favorites)
    }

    fun deleteTracksByPlaylistId(playlistId: Long) {
        val currentTracks = _tracksState.value.toMutableList()
        val updatedTracks = currentTracks.map { track ->
            if (track.playlistId == playlistId) {
                track.copy(playlistId = 0L)
            } else {
                track
            }
        }
        _tracksState.value = updatedTracks
        updatePlaylistsState()
        notifyTracksChanged()
        Log.d("DatabaseMock", "All tracks removed from playlist $playlistId")
    }

    fun searchTracks(expression: String): List<Track> {
        val tracksToReturn = _tracksState.value.filter {
            it.trackName.contains(expression, true) ||
                    it.artistName.contains(expression, true)
        }
        Log.d("DatabaseMock", "searchTracks was called, returning " + tracksToReturn.size + " tracks")
        return tracksToReturn
    }

    fun getTrackById(trackId: Long): Flow<Track?> = callbackFlow {
        var currentTrack: Track? = null

        fun emitCurrentTrack() {
            val track = _tracksState.value.find { it.id == trackId }
            if (currentTrack != track) {
                currentTrack = track
                trySend(track)
            }
        }

        emitCurrentTrack()

        val job = scope.launch {
            _tracksUpdates.collect {
                emitCurrentTrack()
            }
        }

        awaitClose {
            job.cancel()
        }
    }.catch { e ->
        emit(null)
        Log.e("DatabaseMock", "Error in getTrackById", e)
    }

    private fun notifyTracksChanged() {
        scope.launch(Dispatchers.IO) {
            _tracksUpdates.emit(Unit)
        }
    }
}
