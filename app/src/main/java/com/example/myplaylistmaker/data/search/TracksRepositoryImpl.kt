package com.example.myplaylistmaker.data.search

import com.example.myplaylistmaker.data.search.request_and_response.NetworkClient
import com.example.myplaylistmaker.data.search.request_and_response.TrackSearchRequest
import com.example.myplaylistmaker.domain.search.TracksRepository
import com.example.myplaylistmaker.domain.search.models.SearchResult

class TracksRepositoryImpl (private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): SearchResult {
        return networkClient.doRequest(TrackSearchRequest(expression))
    }
}