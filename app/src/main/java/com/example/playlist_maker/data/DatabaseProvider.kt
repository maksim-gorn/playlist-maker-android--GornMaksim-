package com.example.playlist_maker.data

import com.example.playlist_maker.App
import com.example.playlist_maker.data.db.AppDatabase

object DatabaseProvider {
    private var _db: AppDatabase? = null

    fun init(app: App) {
        _db = app.database
    }

    val database: AppDatabase
        get() = _db ?: throw IllegalStateException("DatabaseProvider not initialized")
}