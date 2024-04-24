package com.example.myplaylistmaker.ui.mediaLibrary.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylistmaker.domain.db.FavoritesInteractor
import com.example.myplaylistmaker.domain.search.history.SearchHistoryInteractor
import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val favoritesInteractor: FavoritesInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    private val _favourites = MutableLiveData<List<Track>>()
    val favourites: LiveData<List<Track>> get() = _favourites

    fun loadFavourites() {
        viewModelScope.launch {
//            collect это подписка на flow
            favoritesInteractor.favouritesGet().collect { trackList ->
                _favourites.value = trackList
            }
        }
    }

    fun addItem(item: Track) {
        searchHistoryInteractor.addItem(item)
    }
}

