package com.example.myplaylistmaker.ui.search.view_model_for_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylistmaker.domain.search.ErrorClass
import com.example.myplaylistmaker.domain.search.history.SearchHistoryInteractor
import com.example.myplaylistmaker.domain.search.models.Track
import com.example.myplaylistmaker.domain.search.saerchin_and_responding.SearchInteractor
import com.example.myplaylistmaker.ui.search.view_model_for_activity.screen_state.SearchScreenState
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
) : ViewModel() {
    private val stateLiveData =
        MutableLiveData<SearchScreenState>(SearchScreenState.DefaultSearch)

    private var trackResultList: MutableLiveData<List<Track>?> = MutableLiveData<List<Track>?>()
    fun getStateLiveData(): LiveData<SearchScreenState> {
        return stateLiveData
    }

    //поиск трека
//    private val tracksConsumer = object : SearchInteractor. {
//        override fun consume(result: SearchResult) {
//            when (result) {
//                is SearchResult.Failure -> {
//                    if (result.errorType.error == ERROR_NO_CONNECTION) {
//                        stateLiveData.postValue(SearchScreenState.ConnectionError)
//                    } else {
//                        //можно сделать вывод других исключений, но пока оставлю так
//                        stateLiveData.postValue(SearchScreenState.ConnectionError)
//                    }
//                }
//
//                is SearchResult.Success -> {
//                    if (result.list.isEmpty()) {
//                        stateLiveData.postValue(SearchScreenState.NothingFound)
//                    } else {
//                        stateLiveData.postValue(SearchScreenState.SearchIsOk(result.list))
//                    }
//                }
//            }
//        }
//    }

    suspend fun searchRequesting(searchExpression: String) {
        if (searchExpression.isBlank()) {
            stateLiveData.postValue(SearchScreenState.Loading)
            viewModelScope.launch {
            }
            try {
                searchInteractor.search(searchExpression).collect {
                    when (it.message) {
                        ErrorClass.CONNECTION_ERROR -> stateLiveData.postValue(
                            SearchScreenState.ConnectionError
                        )

                        ErrorClass.SERVER_ERROR -> stateLiveData.postValue(SearchScreenState.NothingFound)
                        else -> {
                            trackResultList.postValue(it.data)
                            stateLiveData.postValue(
                                if (it.data.isNullOrEmpty())
                                    SearchScreenState.NothingFound
                                else SearchScreenState.SearchIsOk(it.data)
                            )
                        }
                    }
                }
            } catch (error: Error) {
                stateLiveData.postValue(SearchScreenState.ConnectionError)
            }
        }
    }


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
        trackHistoryList.value = searchHistoryInteractor.provideHistory()
        if (history.isNullOrEmpty()) {
            trackHistoryList.postValue(emptyList())
        }
        return trackHistoryList
    }

    fun clearTrackList() {
        trackResultList.value = emptyList()
        trackHistoryList.value = searchHistoryInteractor.provideHistory()
        stateLiveData.value =
            trackHistoryList.value?.let { SearchScreenState.SearchWithHistory(it) }
        Log.d("История", trackHistoryList.value.toString())
    }
}







