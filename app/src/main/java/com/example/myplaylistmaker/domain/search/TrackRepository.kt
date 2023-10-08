package com.example.myplaylistmaker.domain.search

import com.example.myplaylistmaker.domain.search.models.SearchResult


interface TracksRepository {
    fun searchTracks(expression: String): SearchResult

}