package com.example.myplaylistmaker.domain.search


import com.bumptech.glide.load.engine.Resource
import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow


interface  TracksRepository {
    fun searchTracks(expression: String): Flow<com.example.myplaylistmaker.data.search.request_and_response.Resource<List<Track>>>

}