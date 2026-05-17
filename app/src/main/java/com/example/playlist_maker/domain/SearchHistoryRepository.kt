package com.example.playlist_maker.domain

import com.example.playlist_maker.ui.search.Word
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {

    suspend fun getHistoryRequests(): List<Word>

    fun addToHistory(word: String)
}