package com.example.myplaylistmaker.domain.newPlaylist

import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class NewPlaylistInteractorImpl(val repository: NewPlaylistRepository) : NewPlaylistRepository,
    NewPlaylistInteractor {
    override fun addPlaylist(
        playlistName: String,
        description: String?,
        uri: String
    ) {
        repository.addPlaylist(playlistName, description, uri)
    }

    override fun deletePlaylist(item: NewPlaylist) {
        repository.deletePlaylist(item)
    }

    override fun queryPlaylist(): Flow<List<NewPlaylist>> {
        return repository.queryPlaylist()
    }

    override fun update(track: Track, playlist: NewPlaylist) {
        repository.update(track, playlist)
    }
}