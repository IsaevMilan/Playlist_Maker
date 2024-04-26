package com.example.myplaylistmaker.domain.tracks_in_playlist

import com.example.myplaylistmaker.domain.playlist.Playlist
import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class TracksInPlaylistInteractorImpl(private val repository: TracksInPlaylistRepository) :
    TracksInPlaylistInteractor {

    override fun getTrackList(playlist: Playlist): Flow<List<Track>> {
        return repository.getTrackList(playlist)
    }

    override fun timeCounting(playlist: Playlist): Flow<String> {
        return repository.timeCounting(playlist)
    }

}