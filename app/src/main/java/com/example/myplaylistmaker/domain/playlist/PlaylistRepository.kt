package com.example.myplaylistmaker.domain.playlist

import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun addPlaylist(playlistName: String,
                    description: String?,
                    uri: String)
    fun deletePlaylist(item:Playlist)
    fun queryPlaylist() : Flow<List<Playlist>>
    fun update(track: Track, playlist: Playlist)
    fun savePlaylist (
        playlist:Playlist,
        playlistName: String,
        description: String?,
        uri: String
    )
    fun findPlaylist(searchId:Int) : Flow<Playlist>
}