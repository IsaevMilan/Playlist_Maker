package com.example.myplaylistmaker.ui.search.view_model_for_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myplaylistmaker.domain.search.history.SearchHistoryInteractor
import com.example.myplaylistmaker.domain.search.models.ERROR_NO_CONNECTION
import com.example.myplaylistmaker.domain.search.models.SearchResult
import com.example.myplaylistmaker.domain.search.models.Track
import com.example.myplaylistmaker.domain.search.saerchin_and_responding.SearchInteractor
import com.example.myplaylistmaker.ui.search.view_model_for_activity.screen_state.SearchScreenState


class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
) : ViewModel() {
    private val stateLiveData =
        MutableLiveData<SearchScreenState>(SearchScreenState.DefaultSearch)

    private val historyLiveData = MutableLiveData<List<Track>>()

    fun historyLiveData(): MutableLiveData<List<Track>> = historyLiveData

    fun getStateLiveData(): LiveData<SearchScreenState> {
        return stateLiveData
    }

    //поиск трека
    private val tracksConsumer = object : SearchInteractor.TracksConsumer {
        override fun consume(result: SearchResult) {
            when (result) {
                is SearchResult.Failure -> {
                    if (result.errorType.error == ERROR_NO_CONNECTION) {
                        stateLiveData.postValue(SearchScreenState.ConnectionError)
                    } else {
                        //можно сделать вывод других исключений, но пока оставлю так
                        stateLiveData.postValue(SearchScreenState.ConnectionError)
                    }
                }

                is SearchResult.Success -> {
                    if (result.list.isEmpty()) {
                        stateLiveData.postValue(SearchScreenState.NothingFound)
                    } else {
                        stateLiveData.postValue(SearchScreenState.SearchIsOk(result.list))
                    }
                }
            }
        }
    }



    fun searchRequesting(searchExpression: String) {
        if (searchExpression.isBlank()) {
            return
        }
        stateLiveData.postValue(SearchScreenState.Loading)
        try {
            searchInteractor.search(searchExpression, tracksConsumer)
        } catch (error: Error) {
            stateLiveData.postValue(SearchScreenState.ConnectionError)
        }
    }



    //история
    private var trackHistoryList: MutableLiveData<List<Track>> =
        MutableLiveData<List<Track>>().apply {
            value = emptyList()
        }

    fun addItem(item: Track) {
        searchHistoryInteractor.addItem(item)
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
    }

    fun provideHistory() {
        trackHistoryList.value = searchHistoryInteractor.provideHistory().orEmpty()
    }

    fun clearTrackList() {
        stateLiveData.value =
            trackHistoryList.value?.let { SearchScreenState.SearchWithHistory(it) }
    }






}