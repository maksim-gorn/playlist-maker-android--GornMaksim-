package com.example.playlist_maker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val query: String,
    val timestamp: Long = System.currentTimeMillis()
)