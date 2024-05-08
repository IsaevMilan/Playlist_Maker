package com.example.myplaylistmaker.domain.playlist

import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(val repository: PlaylistRepository) : PlaylistRepository,
    PlaylistInteractor {
    override fun addPlaylist(
        playlistName: String,
        description: String?,
        uri: String
    ) {
        repository.addPlaylist(playlistName, description, uri)
    }

    override fun deletePlaylist(item: Playlist) {
        repository.deletePlaylist(item)
    }

    override fun queryPlaylist(): Flow<List<Playlist>> {
        return repository.queryPlaylist()
    }

    override fun update(track: Track, playlist: Playlist) {
        repository.update(track, playlist)
    }

    override fun savePlaylist(
        playlist: Playlist,
        playlistName: String,
        description: String?,
        uri: String
    ) {
        repository.savePlaylist(
            playlist,
            playlistName,
            description,
            uri
        )
    }

    override fun findPlaylist(searchId: Int): Flow<Playlist> {
        return repository.findPlaylist(searchId)
    }
}