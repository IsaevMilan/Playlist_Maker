package com.example.myplaylistmaker.ui.mediaLibrary.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylistmaker.domain.db.FavoritesInteractor
import com.example.myplaylistmaker.domain.search.history.SearchHistoryInteractor
import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val favoritesInteractor: FavoritesInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    var trackResultList: MutableLiveData<List<Track>?> = MutableLiveData<List<Track>?>()

    fun favouritesMaker() : LiveData<List<Track>?> {
        viewModelScope.launch {
            while (true) {
                delay (300)
                favoritesInteractor.favouritesGet()
                    .collect { trackList ->
                        if (!trackList.isNullOrEmpty()) {
                            trackResultList.postValue(trackList)
                        } else trackResultList.postValue(emptyList())
                    }
            }
        }
        return trackResultList
    }

    fun addItem(item: Track) {
        searchHistoryInteractor.addItem(item)
    }
}