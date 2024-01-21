package com.example.myplaylistmaker.di.search_module


import com.example.myplaylistmaker.domain.search.history.SearchHistoryInteractor
import com.example.myplaylistmaker.domain.search.history.SearchHistoryInteractorImpl
import com.example.myplaylistmaker.domain.search.saerchin_and_responding.SearchInteractor
import com.example.myplaylistmaker.domain.search.saerchin_and_responding.SearchInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single <SearchInteractor> {
        SearchInteractorImpl ( get())

    }
    single <SearchHistoryInteractor> {
        SearchHistoryInteractorImpl (get())
    }

}