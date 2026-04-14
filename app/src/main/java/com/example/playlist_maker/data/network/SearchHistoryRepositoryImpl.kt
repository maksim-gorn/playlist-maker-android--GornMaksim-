package com.example.playlist_maker.data.network

import com.example.playlist_maker.data.DatabaseMock
import com.example.playlist_maker.domain.SearchHistoryRepository
import com.example.playlist_maker.ui.search.Word
import kotlinx.coroutines.CoroutineScope

class SearchHistoryRepositoryImpl(private val scope: CoroutineScope): SearchHistoryRepository {
    private val database = DatabaseMock(scope = scope)

    override suspend fun getHistoryRequests(): List<Word> {
        return database.getHistory().map { Word(word = it) }
    }

    override fun addToHistory(word: String) {
        database.addToHistory(word = word)
    }
}