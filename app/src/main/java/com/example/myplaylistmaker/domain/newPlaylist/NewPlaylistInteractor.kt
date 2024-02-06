package com.example.myplaylistmaker.domain.newPlaylist

import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface NewPlaylistInteractor {
    fun addPlaylist(
        playlistName: String,
        description: String?,
        uri: String
    )

    fun deletePlaylist(item: NewPlaylist)
    fun queryPlaylist(): Flow<List<NewPlaylist>>
    fun update(track: Track, playlist: NewPlaylist)
}