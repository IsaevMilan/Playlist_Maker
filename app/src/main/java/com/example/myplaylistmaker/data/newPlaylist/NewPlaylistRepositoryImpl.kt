package com.example.myplaylistmaker.data.newPlaylist

import com.example.myplaylistmaker.data.converters.NewPlaylistConverter
import com.example.myplaylistmaker.data.db.NewPlaylistDatabase
import com.example.myplaylistmaker.data.db.TrackInPlaylistDatabase
import com.example.myplaylistmaker.domain.newPlaylist.NewPlaylist
import com.example.myplaylistmaker.domain.newPlaylist.NewPlaylistRepository
import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewPlaylistRepositoryImpl (

    private val newPlaylistDatabase: NewPlaylistDatabase,
    private val converter: NewPlaylistConverter,
    private val trackInDatabase: TrackInPlaylistDatabase
) : NewPlaylistRepository {

    override fun addPlaylist(
        playlistName: String,
        description: String?,
        uri: String
    ) {
        val playlist = NewPlaylist(
            null,
            playlistName,
            description,
            uri,
            emptyList(),
            0
        )
        newPlaylistDatabase.playlistDao().insertPlaylist(
            converter.mapplaylistClassToEntity(playlist)
        )
    }

    override fun deletePlaylist(item: NewPlaylist) {
        converter.mapplaylistClassToEntity(item)
            ?.let { newPlaylistDatabase.playlistDao().deletePlaylist(it) }
    }

    override fun queryPlaylist(): Flow<List<NewPlaylist>> = flow {
        val playlistConverted =
            newPlaylistDatabase.playlistDao().queryPlaylist()
                .map { converter.mapplaylistEntityToClass(it) }
        emit(playlistConverted)
        return@flow

    }

    override fun update(track: Track, playlist: NewPlaylist) {

        newPlaylistDatabase.playlistDao().updatePlaylist(converter.mapplaylistClassToEntity(playlist))
        trackInDatabase.tracklistDao().insertTrack(track)

    }
}
