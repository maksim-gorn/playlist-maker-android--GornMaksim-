package com.example.playlist_maker.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker.data.network.RetrofitNetworkClient
import com.example.playlist_maker.data.network.SearchHistoryRepositoryImpl
import com.example.playlist_maker.data.network.TracksRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel() : ViewModel() {
    private val networkClient = RetrofitNetworkClient.create()
    private val tracksRepository = TracksRepositoryImpl(networkClient)
    private val searchHistoryRepository = SearchHistoryRepositoryImpl()
    private val _searchQuery = MutableStateFlow("")
    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState = _searchScreenState.asStateFlow()

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(1000)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isNotEmpty()) {
                        performSearch(query)
                    }
                }
        }
    }

    fun updateQuery(query: String) {
        _searchQuery.value = query
    }

    private fun performSearch(request: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchScreenState.update { SearchState.Searching }
                searchHistoryRepository.addToHistory(request)
                val list = tracksRepository.searchTracks(expression = request)
                _searchScreenState.update { SearchState.Success(foundList = list) }
            } catch (e: IOException) {
                _searchScreenState.update { SearchState.Fail(e.message.toString()) }
            }
        }
    }

    fun clearSearch() {
        _searchScreenState.update { SearchState.Initial }
    }

    suspend fun getHistoryList() : List<Word> {
       return searchHistoryRepository.getHistoryRequests()
    }
}