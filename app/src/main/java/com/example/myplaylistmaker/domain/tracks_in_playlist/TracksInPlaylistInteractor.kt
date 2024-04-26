package com.example.myplaylistmaker.domain.tracks_in_playlist

import com.example.myplaylistmaker.domain.playlist.Playlist
import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInPlaylistInteractor {

        fun getTrackList (playlist: Playlist) : Flow<List<Track>>
        fun timeCounting (playlist: Playlist) : Flow<String>

}