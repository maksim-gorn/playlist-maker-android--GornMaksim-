package com.example.playlist_maker.data.network

import com.example.playlist_maker.data.DatabaseProvider
import com.example.playlist_maker.data.Playlist
import com.example.playlist_maker.data.db.PlaylistEntity
import com.example.playlist_maker.domain.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl : PlaylistsRepository {
    private val playlistDao = DatabaseProvider.database.playlistDao()
    private val trackDao = DatabaseProvider.database.trackDao()

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return playlistDao.getPlaylist(playlistId).flatMapLatest { entity ->
            if (entity == null) flowOf(null)
            else {
                trackDao.getTracksByPlaylistId(playlistId).map { tracks ->
                    Playlist(
                        id = entity.id,
                        name = entity.name,
                        description = entity.description,
                        tracks = tracks.map { it.toDomain() }.toMutableList()
                    )
                }
            }
        }
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylists().flatMapLatest { entities ->
            if (entities.isEmpty()) flowOf(emptyList())
            else {
                val trackFlows = entities.map { entity ->
                    trackDao.getTracksByPlaylistId(entity.id).map { tracks ->
                        entity to tracks
                    }
                }
                combine(trackFlows) { results ->
                    results.map { (entity, tracks) ->
                        Playlist(
                            id = entity.id,
                            name = entity.name,
                            description = entity.description,
                            tracks = tracks.map { it.toDomain() }.toMutableList()
                        )
                    }
                }
            }
        }
    }

    override suspend fun addNewPlaylist(name: String, description: String) {
        playlistDao.insertPlaylist(PlaylistEntity(name = name, description = description))
    }

    override suspend fun deletePlaylistById(id: Long) {
        trackDao.removeAllFromPlaylist(id)
        playlistDao.deletePlaylist(id)
    }
}