package com.example.myplaylistmaker.domain.search.saerchin_and_responding

import com.example.myplaylistmaker.domain.search.TracksRepository
import com.example.myplaylistmaker.domain.search.models.Track

class SearchInteractorImpl(private val repository: TracksRepository) : SearchInteractor {
    override fun search(
        expression: String,
        consumer: SearchInteractor.TracksConsumer
    ){
        Thread {
            consumer.consume(repository.searchTracks(expression))
        }.start()
    }
}