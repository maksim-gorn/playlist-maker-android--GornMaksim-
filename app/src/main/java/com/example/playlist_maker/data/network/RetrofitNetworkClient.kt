package com.example.playlist_maker.data.network

import com.example.playlist_maker.creator.Storage
import com.example.playlist_maker.data.dto.TracksSearchRequest
import com.example.playlist_maker.data.dto.TracksSearchResponse
import com.example.playlist_maker.domain.NetworkClient

class RetrofitNetworkClient(private val storage: Storage) : NetworkClient {

    override fun doRequest(request: Any): TracksSearchResponse {
        val searchList = storage.search((request as TracksSearchRequest).expression)
        return TracksSearchResponse(searchList).apply { resultCode = 200 }
    }
}