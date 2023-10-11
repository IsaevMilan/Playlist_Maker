package com.example.myplaylistmaker.ui.search.view_model_for_activity.screen_state

import com.example.myplaylistmaker.domain.search.models.Track

sealed class SearchScreenState {
    object DefaultSearch : SearchScreenState()
    object Loading : SearchScreenState()
    object NothingFound : SearchScreenState()
    object ConnectionError : SearchScreenState()
    data class SearchWithHistory(var historyData: List<Track>) : SearchScreenState()
    data class SearchIsOk(val data: List<Track>) : SearchScreenState()
}