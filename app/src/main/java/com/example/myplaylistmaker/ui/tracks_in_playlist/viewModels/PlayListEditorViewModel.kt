package com.example.myplaylistmaker.ui.tracks_in_playlist.viewModels

import androidx.lifecycle.ViewModel
import com.example.myplaylistmaker.domain.playlist.Playlist
import com.example.myplaylistmaker.domain.playlist.PlaylistInteractor

class PlayListEditorViewModel (   private val playlistInteractor: PlaylistInteractor

) : ViewModel() {

    fun savePlayList(
        playlist: Playlist,
        playlistName: String,
        description: String?,
        uri: String
    ) {
        playlistInteractor.savePlaylist(playlist, playlistName, description, uri)
    }
    fun deletePlaylist (playlist: Playlist) {
        playlistInteractor.deletePlaylist(playlist)
    }
}