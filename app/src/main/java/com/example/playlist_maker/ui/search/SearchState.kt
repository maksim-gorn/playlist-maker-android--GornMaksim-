package com.example.playlist_maker.ui.search

import com.example.playlist_maker.data.network.Track

sealed class SearchState {
    object Initial: SearchState() // Первоначальное cостояние экрана
    object Searching: SearchState() // Cостояние экрана при начале поиска
    data class Success(val foundList: List<Track>): SearchState() // Cостояние экрана при успешном завершении поиска
    data class Fail(val error: String): SearchState() // Cостояние экрана, если при запросе к серверу произошла ошибка
}