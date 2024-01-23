package com.example.myplaylistmaker.ui.mediaLibrary.viewModels

import androidx.lifecycle.ViewModel
import com.example.myplaylistmaker.domain.newPlaylist.NewPlaylist
import com.example.myplaylistmaker.domain.newPlaylist.NewPlaylistInteractor
import com.example.myplaylistmaker.domain.settings.SettingsInteractor

class NewPlaylistViewModel(private val interactor: NewPlaylistInteractor, private val settingsInteractor: SettingsInteractor) : ViewModel() {

    fun addPlayList(
        playlistName: String,
        description: String?,
        uri: String
    ) {
        interactor.addPlaylist(playlistName, description, uri)
    }

    fun deletePlaylist(item: NewPlaylist) {
        interactor.deletePlaylist(item)
    }

    fun isAppThemeDark() :Boolean{
        return settingsInteractor.isAppThemeDark()
    }
}