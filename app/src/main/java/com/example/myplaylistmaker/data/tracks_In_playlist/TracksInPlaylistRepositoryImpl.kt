package com.example.myplaylistmaker.data.tracks_In_playlist

import com.example.myplaylistmaker.data.converters.TrackConvertor
import com.example.myplaylistmaker.data.db.TrackInPlaylistDatabase
import com.example.myplaylistmaker.domain.playlist.Playlist
import com.example.myplaylistmaker.domain.search.models.Track
import com.example.myplaylistmaker.domain.tracks_in_playlist.TracksInPlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksInPlaylistRepositoryImpl(
    private val base: TrackInPlaylistDatabase
) : TracksInPlaylistRepository {

    override fun getTrackList(playlist: Playlist): Flow<List<Track>> = flow {
        val trackList = playlist.trackArray.mapNotNull { id ->
            val entity = id?.let { base.tracklistDao().queryTrackId(searchId = it) }
            entity?.let { TrackConvertor().mapTrackEntityToTrack(it) }
        }
        emit(trackList)
    }

    override fun timeCounting(playlist: Playlist): Flow<String> = flow {
        var generalTime = 0
        playlist.trackArray.forEach { id ->
            val entity = id?.let { base.tracklistDao().queryTrackId(searchId = it) }
            val track = entity?.let { TrackConvertor().mapTrackEntityToTrack(it) }
            val time = track?.trackTimeMillis
            val trackSeconds =
                (time?.split(":")?.getOrNull(0)?.toIntOrNull() ?: 0) * 60 + (time?.split(":")
                    ?.getOrNull(1)
                    ?.toIntOrNull()
                    ?: 0)
            generalTime += trackSeconds
        }
        val hours = generalTime / (60 * 60)
        val minutes = (generalTime / 60) % 60
        val seconds = generalTime % 60
        val readyTime = if (hours == 0) {
            String.format("%02d:%02d", minutes, seconds)
        } else {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
        emit(readyTime)
    }
}
