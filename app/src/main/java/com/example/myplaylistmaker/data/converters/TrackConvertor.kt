package com.example.myplaylistmaker.data.converters

import com.example.myplaylistmaker.data.db.entity.TrackEntity
import com.example.myplaylistmaker.data.db.entity.TrackInPlaylistEntity
import com.example.myplaylistmaker.domain.search.models.Track

class TrackConvertor {
    fun mapTrackToFavourite(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.addTime,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavorite
        )
    }

    fun mapFavouriteToTrack(track: TrackEntity): Track {
        return Track(
            track.trackName,
            track.addTime,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.trackId,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavorite
        )
    }
    fun mapTrackEntityToTrack(entity: TrackInPlaylistEntity): Track {
        return Track(
            entity.trackName,
            entity.addTime,
            entity.artistName,
            entity.trackTimeMillis,
            entity.artworkUrl100,
            entity.trackId,
            entity.collectionName,
            entity.releaseDate,
            entity.primaryGenreName,
            entity.country,
            entity.previewUrl,
            entity.isFavorite
        )
    }
}