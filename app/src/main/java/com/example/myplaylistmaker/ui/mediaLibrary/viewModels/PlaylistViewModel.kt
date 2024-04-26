package com.example.myplaylistmaker.ui.mediaLibrary.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylistmaker.domain.playlist.Playlist
import com.example.myplaylistmaker.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(private val interactor: PlaylistInteractor) : ViewModel() {
    val playlist: MutableLiveData<List<Playlist>> = MutableLiveData()

    fun getPlaylist() {
        viewModelScope.launch {

            interactor.queryPlaylist()
                .collect {
                    if (it.isNotEmpty()) {
                        playlist.postValue(it)
                    } else {
                        playlist.postValue(emptyList())
                    }
                }
        }

    }
    fun deletePlaylist (item:Playlist){
        interactor.deletePlaylist(item)
    }
}