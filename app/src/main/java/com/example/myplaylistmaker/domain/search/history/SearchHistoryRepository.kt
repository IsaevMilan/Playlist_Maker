package com.example.myplaylistmaker.domain.search.history

import com.example.myplaylistmaker.domain.search.models.Track

interface SearchHistoryRepository {
    fun addItem(newHistoryTrack: Track)
    fun clearHistory()
    fun provideHistory(): List<Track>
}