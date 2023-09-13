package com.example.myplaylistmaker.domain.search.history

import com.example.myplaylistmaker.domain.search.models.Track


interface SearchHistoryInteractor {
    fun addItem(item: Track)
    fun clearHistory()
    fun provideHistory(): List<Track>?
}