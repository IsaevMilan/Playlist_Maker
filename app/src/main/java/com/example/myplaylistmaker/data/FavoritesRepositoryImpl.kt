package com.example.myplaylistmaker.data

import android.util.Log
import com.example.myplaylistmaker.data.converters.TrackConvertor
import com.example.myplaylistmaker.data.db.AppDatabase
import com.example.myplaylistmaker.domain.db.FavouritesRepository
import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouritesRepositoryImpl(
    private val dataBase: AppDatabase,
    private val converter: TrackConvertor
) : FavouritesRepository {

    override fun addTrack(track: Track) {
        track.isFavorite = true
        track.addTime = System.currentTimeMillis()
        dataBase.trackDao().insertTrack(track)
    }

    override fun deleteTrack(track: Track) {
        track.isFavorite = false
        converter.mapTrackToFavourite(track)?.let { dataBase.trackDao().deleteTrack(it) }
    }

    override fun getFavourites(): Flow<List<Track>> = flow {
        val favourites = dataBase.trackDao().queryTrack()

        Log.d("Избранное репозиторий", favourites.toString())
        if (favourites != null) {
            val favouritesConverted =
                dataBase.trackDao().queryTrack().map { converter.mapFavouriteToTrack(it) }
            emit(favouritesConverted)
        } else {
            emit(emptyList())
        }
    }


    override fun checkFavourites(id: Long): Flow<Boolean> = flow {
        emit(dataBase.trackDao().queryTrackId(id) != null)
    }
}