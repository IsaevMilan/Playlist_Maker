package com.example.myplaylistmaker.domain.search

import com.example.myplaylistmaker.domain.search.models.Track


interface TracksRepository {
    fun searchTracks (expression:String) :List<Track>

}