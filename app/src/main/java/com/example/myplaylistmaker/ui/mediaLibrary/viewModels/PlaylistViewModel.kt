package com.example.myplaylistmaker.ui.mediaLibrary.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylistmaker.domain.newPlaylist.NewPlaylist
import com.example.myplaylistmaker.domain.newPlaylist.NewPlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(private val interactor: NewPlaylistInteractor) : ViewModel() {
    val playlistList: MutableLiveData<List<NewPlaylist>> = MutableLiveData<List<NewPlaylist>>()
    fun playlistMaker(): LiveData<List<NewPlaylist>> {
        viewModelScope.launch {
            interactor.queryPlaylist()
                .collect {
                    if (it.isNotEmpty()) {
                        playlistList.postValue(it)
                    } else {
                        playlistList.postValue(emptyList())
                    }
                }
        }
        return playlistList
    }

}