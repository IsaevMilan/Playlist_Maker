package com.example.myplaylistmaker.domain.search.saerchin_and_responding

import com.example.myplaylistmaker.domain.search.models.SearchResult


interface SearchInteractor {
    fun search (expression:String, consumer: TracksConsumer)
    interface TracksConsumer{
        fun consume(result: SearchResult)
    }
}