package com.example.myplaylistmaker.di.search_module

import com.example.myplaylistmaker.data.search.TracksRepositoryImpl
import com.example.myplaylistmaker.data.search.history.SearchHistoryImpl
import com.example.myplaylistmaker.domain.search.TracksRepository
import com.example.myplaylistmaker.domain.search.history.SearchHistory
import org.koin.dsl.module

val RepositoryModule = module {

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single <SearchHistory> {
        SearchHistoryImpl(get(), get())
    }
}
