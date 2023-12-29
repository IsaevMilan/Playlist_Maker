package com.example.myplaylistmaker.data.search


import com.example.myplaylistmaker.data.search.request_and_response.NetworkClient
import com.example.myplaylistmaker.data.search.request_and_response.Resource
import com.example.myplaylistmaker.data.search.request_and_response.TrackResponse
import com.example.myplaylistmaker.data.search.request_and_response.TrackSearchRequest
import com.example.myplaylistmaker.domain.search.ErrorClass
import com.example.myplaylistmaker.domain.search.TracksRepository
import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl (private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow{
        try {
            val response = networkClient.doRequest(TrackSearchRequest(expression))
            when (response.resultCode) {
                -1 -> {
                   emit(Resource.Error(ErrorClass.CONNECTION_ERROR))
                }

                200 -> {
                      emit(Resource.Success((response as TrackResponse).results.map {
                            Track(
                                it.trackName,
                                it.artistName,

                                SimpleDateFormat(
                                    "mm:ss",
                                    Locale.getDefault()
                                ).format(it.trackTimeMillis),
                                it.artworkUrl100,
                                it.trackId,
                                it.collectionName,
                                it.releaseDate,
                                it.primaryGenreName,
                                it.country,
                                it.previewUrl
                            )

                }))
            }

            else -> {

            emit(Resource.Error(ErrorClass.SERVER_ERROR))
        }
    }

    } catch (error: Error) {
        throw Exception(error)
    }
    }
}