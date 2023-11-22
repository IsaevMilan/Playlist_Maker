package com.example.myplaylistmaker.domain.search.saerchin_and_responding


import com.example.myplaylistmaker.domain.search.TracksRepository

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