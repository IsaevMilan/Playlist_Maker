package com.example.myplaylistmaker.domain.search.history

import com.example.myplaylistmaker.domain.search.models.Track


class SearchHistoryInteractorImpl(private val historyRepository: SearchHistoryRepository) :
    SearchHistoryInteractor {

    override fun addItem(item: Track) {
        historyRepository.addItem(item)
    }

    override fun clearHistory() {
        historyRepository.clearHistory()
    }

    override fun provideHistory(): List<Track> {
        return historyRepository.provideHistory()
    }
}