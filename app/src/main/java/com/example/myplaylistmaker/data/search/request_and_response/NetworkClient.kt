package com.example.myplaylistmaker.data.search.request_and_response

import com.example.myplaylistmaker.domain.search.models.SearchResult


interface NetworkClient {
    fun doRequest(request: TrackSearchRequest): SearchResult
}