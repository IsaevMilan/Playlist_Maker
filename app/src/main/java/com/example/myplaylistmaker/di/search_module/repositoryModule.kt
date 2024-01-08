package com.example.myplaylistmaker.di.search_module

import com.example.myplaylistmaker.data.converters.TrackConvertor
import com.example.myplaylistmaker.data.search.TracksRepositoryImpl
import com.example.myplaylistmaker.data.search.history.SearchHistoryRepositoryImpl
import com.example.myplaylistmaker.domain.search.TracksRepository
import com.example.myplaylistmaker.domain.search.history.SearchHistoryRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single <SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    factory { TrackConvertor() }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get(), get())
    }
}
