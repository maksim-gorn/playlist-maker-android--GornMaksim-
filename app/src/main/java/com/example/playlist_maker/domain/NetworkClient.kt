package com.example.playlist_maker.domain

import com.example.playlist_maker.data.dto.BaseResponse

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
}