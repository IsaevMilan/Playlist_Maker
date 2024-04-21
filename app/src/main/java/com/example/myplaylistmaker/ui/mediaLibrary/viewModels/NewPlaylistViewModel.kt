package com.example.myplaylistmaker.ui.mediaLibrary.viewModels

import androidx.lifecycle.ViewModel
import com.example.myplaylistmaker.domain.playlist.Playlist
import com.example.myplaylistmaker.domain.playlist.PlaylistInteractor
import com.example.myplaylistmaker.domain.settings.SettingsInteractor

class NewPlaylistViewModel(private val interactor: PlaylistInteractor, private val settingsInteractor: SettingsInteractor) : ViewModel() {

    fun addPlayList(
        playlistName: String,
        description: String?,
        uri: String
    ) {

        interactor.addPlaylist(playlistName, description, uri)
    }

    fun deletePlaylist(item: Playlist) {
        interactor.deletePlaylist(item)
    }

    fun isAppThemeDark() :Boolean{
        return settingsInteractor.isAppThemeDark()
    }
}