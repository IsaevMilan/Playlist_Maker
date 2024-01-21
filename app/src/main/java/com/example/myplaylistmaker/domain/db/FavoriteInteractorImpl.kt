package com.example.myplaylistmaker.domain.db


import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val favouritesRepository : FavouritesRepository) : FavoritesInteractor{

    override fun favouritesAdd(track: Track) {
        favouritesRepository.addTrack(track)
    }

    override fun favouritesDelete(track: Track) {
        favouritesRepository.deleteTrack(track)
    }

    override fun favouritesGet(): Flow<List<Track>> {
        return favouritesRepository.getFavourites()
    }

    override fun favouritesCheck(id: Long): Flow<Boolean> {
        return favouritesRepository.checkFavourites(id)
    }
}