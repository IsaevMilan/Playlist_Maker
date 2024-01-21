package com.example.myplaylistmaker.domain.db

import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {
    fun addTrack(track: Track)
    fun deleteTrack(track: Track)
    fun getFavourites(): Flow<List<Track>>
    fun checkFavourites(id: Long): Flow<Boolean>
}