package com.example.playlist_maker.ui.search

import com.example.playlist_maker.creator.Storage
import com.example.playlist_maker.data.network.RetrofitNetworkClient
import com.example.playlist_maker.data.network.TracksRepositoryImpl
import com.example.playlist_maker.domain.TracksRepository
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.EmptyCoroutineContext

object Creator {
    fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(CoroutineScope(EmptyCoroutineContext)) //здесь надо разобраться
    }
}