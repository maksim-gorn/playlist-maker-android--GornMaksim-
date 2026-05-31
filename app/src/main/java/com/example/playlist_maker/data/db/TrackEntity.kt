package com.example.playlist_maker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val image: String,
    val previewUrl: String? = null,
    val isFavorite: Boolean = false,
    val playlistId: Long = 0L
)