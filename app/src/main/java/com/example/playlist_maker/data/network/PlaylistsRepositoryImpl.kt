package com.example.playlist_maker.data.network

import com.example.playlist_maker.data.DatabaseProvider
import com.example.playlist_maker.data.Playlist
import com.example.playlist_maker.data.db.PlaylistEntity
import com.example.playlist_maker.domain.PlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl : PlaylistsRepository {
    private val playlistDao = DatabaseProvider.database.playlistDao()
    private val trackDao = DatabaseProvider.database.trackDao()

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return playlistDao.getPlaylist(playlistId).map { entity ->
            if (entity == null) null
            else {
                val tracks = trackDao.getTracksByPlaylistId(playlistId).first()
                Playlist(
                    id = entity.id,
                    name = entity.name,
                    description = entity.description,
                    tracks = tracks.map { it.toDomain() }.toMutableList()
                )
            }
        }
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylists().map { entities ->
            entities.map { entity ->
                val tracks = trackDao.getTracksByPlaylistId(entity.id).first()
                Playlist(
                    id = entity.id,
                    name = entity.name,
                    description = entity.description,
                    tracks = tracks.map { it.toDomain() }.toMutableList()
                )
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