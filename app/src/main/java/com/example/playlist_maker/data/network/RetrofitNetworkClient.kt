package com.example.playlist_maker.data.network

import com.example.playlist_maker.data.dto.BaseResponse
import com.example.playlist_maker.data.dto.TracksSearchRequest
import com.example.playlist_maker.data.dto.TracksSearchResponse
import com.example.playlist_maker.domain.NetworkClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class RetrofitNetworkClient(private val api: ITunesApiService) : NetworkClient {

    override suspend fun doRequest(dto: Any): BaseResponse {
        return try {
            when (dto) {
                is TracksSearchRequest -> {
                    val response = api.searchTracks(
                        query = dto.expression,
                        media = "music",
                        entity = "song",
                        limit = 50
                    )
                    response.resultCode = 200
                    response
                }

                else -> BaseResponse().apply {
                    resultCode = 400
                }
            }
        } catch (e: IOException) {
            BaseResponse().apply { resultCode = -1 }
        } catch (e: Exception) {
            BaseResponse().apply { resultCode = -2 }
        }
    }

    companion object {
        private const val BASE_URL = "https://itunes.apple.com/"

        fun create(): RetrofitNetworkClient {
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ITunesApiService::class.java)
            return RetrofitNetworkClient(apiService)
        }
    }
}