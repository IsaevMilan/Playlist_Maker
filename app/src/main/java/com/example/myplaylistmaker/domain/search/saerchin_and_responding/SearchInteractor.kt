package com.example.myplaylistmaker.domain.search.saerchin_and_responding

import com.example.myplaylistmaker.data.search.request_and_response.Resource
import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow


interface SearchInteractor {
     suspend fun search(expression: String): Flow<Resource<List<Track>>>
}