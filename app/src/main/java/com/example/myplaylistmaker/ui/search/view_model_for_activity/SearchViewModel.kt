package com.example.myplaylistmaker.ui.search.view_model_for_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylistmaker.domain.search.ErrorClass
import com.example.myplaylistmaker.domain.search.history.SearchHistoryInteractor
import com.example.myplaylistmaker.domain.search.models.Track
import com.example.myplaylistmaker.domain.search.saerchin_and_responding.SearchInteractor
import com.example.myplaylistmaker.ui.search.view_model_for_activity.screen_state.SearchScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
) : ViewModel() {
    private val stateLiveData =
        MutableLiveData<SearchScreenState>(SearchScreenState.DefaultSearch)

    private val isClickAllowed = MutableLiveData(true)

    private var trackResultList: MutableLiveData<List<Track>?> = MutableLiveData<List<Track>?>()

    fun getStateLiveData(): LiveData<SearchScreenState> {
        return stateLiveData
    }

    suspend fun searchRequesting(searchExpression: String) {
        if (searchExpression.isNotBlank()) {
            stateLiveData.postValue(SearchScreenState.Loading)
            viewModelScope.launch {
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
    }


    private var trackHistoryList: MutableLiveData<List<Track>> =
        MutableLiveData<List<Track>>().apply {
            value = emptyList()
        }

    fun getIsClickAllowed(): LiveData<Boolean> = isClickAllowed

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

    fun clickDebouncer() {

        if (isClickAllowed.value == true) {
            viewModelScope.launch {
                isClickAllowed.value = false
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed.value = true
            }
        }
    }


    fun onChangeFocus(hasFocus: Boolean, text: String) {

        if (hasFocus && text.isEmpty()) {
            getHistory()

        } else if (text.isEmpty()) {

            stateLiveData.value = SearchScreenState.DefaultSearch

        }
    }

    fun getHistory() {
        val history = searchHistoryInteractor.provideHistory()
        val newState = if (!history.isNullOrEmpty()) {
            SearchScreenState.SearchWithHistory(history)
        } else {
            SearchScreenState.DefaultSearch
        }
        stateLiveData.postValue(newState)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}









