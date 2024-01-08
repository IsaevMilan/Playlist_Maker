package com.example.myplaylistmaker.domain.db

import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    fun favouritesAdd (track: Track)
    fun favouritesDelete (track: Track)
    fun favouritesGet(): Flow<List<Track>>
    fun favouritesCheck(id:Long):Flow<Boolean>
}