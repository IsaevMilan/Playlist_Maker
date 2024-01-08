package com.example.myplaylistmaker.domain.search.saerchin_and_responding


import com.example.myplaylistmaker.data.search.request_and_response.Resource
import com.example.myplaylistmaker.domain.search.TracksRepository
import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(private val repository: TracksRepository) : SearchInteractor {
    override suspend fun search(expression: String): Flow<Resource<List<Track>>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    (Resource.Success(result.data))
                }

                is Resource.Error<*> -> {
                    Resource.Error(null, result.message)
                }
            } as Resource<List<Track>>
        }
    }

}