package com.example.playlist_maker.ui.search

import com.example.playlist_maker.data.network.RetrofitNetworkClient
import com.example.playlist_maker.data.network.TracksRepositoryImpl
import com.example.playlist_maker.domain.TracksRepository

object Creator {
    fun getTracksRepository(): TracksRepository {
        val networkClient = RetrofitNetworkClient.create()
        return TracksRepositoryImpl(networkClient)
    }
}