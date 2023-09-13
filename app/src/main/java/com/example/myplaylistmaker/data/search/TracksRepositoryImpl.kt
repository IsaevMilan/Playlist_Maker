package com.example.myplaylistmaker.data.search

import com.example.myplaylistmaker.data.search.request_and_response.NetworkClient
import com.example.myplaylistmaker.data.search.request_and_response.TrackResponse
import com.example.myplaylistmaker.data.search.request_and_response.TrackSearchRequest
import com.example.myplaylistmaker.domain.search.TracksRepository
import com.example.myplaylistmaker.domain.search.models.Track
import java.lang.Error
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Locale


class TracksRepositoryImpl (private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): List<Track> {
        try {
            val response = networkClient.doRequest(TrackSearchRequest(expression))
            if (response.resultCode != 200) {
                return emptyList()
            }
            return (response as TrackResponse).results.map {
                Track(
                    it.trackName,
                    it.artistName,
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis),
                    it.artworkUrl100,
                    it.trackId,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
        } catch(error: Error) {
            throw Exception(error)
        }
    }
}