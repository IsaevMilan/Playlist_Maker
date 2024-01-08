package com.example.myplaylistmaker.data.search.request_and_response

import com.example.myplaylistmaker.data.Mapper
import com.example.myplaylistmaker.domain.search.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class SearchMapper : Mapper<TrackDto, Track> {

    override fun mapItem(item: TrackDto): Track {
        return Track(
            trackName = item.trackName,
            addTime = item.addTime,
            artistName = item.artistName,
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis),
            artworkUrl100 = item.artworkUrl100,
            trackId = item.trackId,
            collectionName = item.collectionName,
            releaseDate = item.releaseDate,
            primaryGenreName = item.primaryGenreName,
            country = item.country,
            previewUrl = item.previewUrl
        )
    }
}