package com.example.playlist_maker.domain

import com.example.playlist_maker.data.dto.BaseResponse

interface NetworkClient {
    suspend fun doRequest(dto: Any): BaseResponse
}