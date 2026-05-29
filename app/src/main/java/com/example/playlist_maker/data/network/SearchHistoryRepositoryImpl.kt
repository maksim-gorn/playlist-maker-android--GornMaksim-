package com.example.playlist_maker.data.network

import com.example.playlist_maker.data.DatabaseProvider
import com.example.playlist_maker.data.db.HistoryEntity
import com.example.playlist_maker.domain.SearchHistoryRepository
import com.example.playlist_maker.ui.search.Word
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SearchHistoryRepositoryImpl : SearchHistoryRepository {
    private val historyDao = DatabaseProvider.database.historyDao()

    override suspend fun getHistoryRequests(): List<Word> {
        return historyDao.getRecentHistory().first().map { Word(word = it.query) }
    }

    override fun addToHistory(word: String) {
        CoroutineScope(Dispatchers.IO).launch {
            historyDao.deleteByQuery(word)
            historyDao.insertHistory(HistoryEntity(query = word))
            val count = historyDao.getCount()
            if (count > 10) {
                historyDao.deleteOldest()
            }
        }
    }
}