package com.example.myplaylistmaker.ui.mediaLibrary.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylistmaker.domain.playlist.Playlist
import com.example.myplaylistmaker.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(private val interactor: PlaylistInteractor) : ViewModel() {
    val playlistList: MutableLiveData<List<Playlist>> = MutableLiveData()

    fun playlistMaker(): LiveData<List<Playlist>> {
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