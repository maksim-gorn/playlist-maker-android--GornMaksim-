package com.example.playlist_maker

import android.app.Application
import com.example.playlist_maker.data.DatabaseProvider
import com.example.playlist_maker.data.db.AppDatabase

class App : Application() {

    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(this)
        DatabaseProvider.init(this)
    }
}