package com.example.myplaylistmaker.ui.search.view_model_for_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myplaylistmaker.creator.Creator
import com.example.myplaylistmaker.domain.search.history.SearchHistoryInteractor
import com.example.myplaylistmaker.domain.search.models.Track
import com.example.myplaylistmaker.domain.search.saerchin_and_responding.SearchInteractor
import com.example.myplaylistmaker.ui.search.view_model_for_activity.screen_state.SearchScreenState

class SearchViewModel(
    private var searchInteractor: SearchInteractor,
    private var searchHistoryInteractor: SearchHistoryInteractor,
) : ViewModel() {
    private var stateLiveData =
        MutableLiveData<SearchScreenState>(SearchScreenState.DefaultSearch)

    fun getStateLiveData(): LiveData<SearchScreenState> {
        return stateLiveData
    }

    //поиск трека
    private val tracksConsumer = object : SearchInteractor.TracksConsumer {
        override fun consume(tracks: List<Track>) {
            trackResultList.postValue(tracks)
            stateLiveData.postValue(
                if (tracks.isNullOrEmpty())
                    SearchScreenState.NothingFound
                else SearchScreenState.SearchIsOk(tracks)
            )
        }
    }

    private var trackResultList: MutableLiveData<List<Track>> = MutableLiveData<List<Track>>()
    fun searchRequesting(searchExpression: String) {
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

    fun provideHistory(): LiveData<List<Track>> {
        val history = searchHistoryInteractor.provideHistory()
        trackHistoryList.value=history
        if (history.isNullOrEmpty()) {
            trackHistoryList.postValue(emptyList())
        }
        return trackHistoryList
    }

    fun clearTrackList() {
        trackResultList.value = emptyList()
        stateLiveData.value= trackHistoryList.value?.let { SearchScreenState.SearchWithHistory(it) }
    }


    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(
                        Creator.provideSearchInteractor(),
                        Creator.provideSearchHistoryInteractor(),
                    ) as T
                }
            }
    }
}