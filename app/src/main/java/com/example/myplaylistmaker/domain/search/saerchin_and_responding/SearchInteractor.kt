package com.example.myplaylistmaker.domain.search.saerchin_and_responding

import com.example.myplaylistmaker.domain.search.models.Track

interface SearchInteractor {
    fun search (expression:String, consumer: TracksConsumer)
    interface TracksConsumer{
        fun consume(findTracks: List<Track>)
    }
}