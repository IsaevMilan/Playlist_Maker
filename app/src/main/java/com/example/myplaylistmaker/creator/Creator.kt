package com.example.myplaylistmaker.creator

import com.example.myplaylistmaker.app.App
import com.example.myplaylistmaker.data.player.PlayerRepositoryImpl
import com.example.myplaylistmaker.data.search.TracksRepositoryImpl
import com.example.myplaylistmaker.data.search.history.SearchHistoryImpl
import com.example.myplaylistmaker.data.search.request_and_response.RetrofitNetworkClient
import com.example.myplaylistmaker.data.settings.ThemeSettingsImpl
import com.example.myplaylistmaker.data.sharing.ExternalNavigatorImpl
import com.example.myplaylistmaker.domain.player.PlayerInteractor
import com.example.myplaylistmaker.domain.player.PlayerInteractorImpl
import com.example.myplaylistmaker.domain.player.PlayerRepository
import com.example.myplaylistmaker.domain.search.TracksRepository
import com.example.myplaylistmaker.domain.search.history.SearchHistory
import com.example.myplaylistmaker.domain.search.history.SearchHistoryInteractor
import com.example.myplaylistmaker.domain.search.history.SearchHistoryInteractorImpl
import com.example.myplaylistmaker.domain.search.saerchin_and_responding.SearchInteractor
import com.example.myplaylistmaker.domain.search.saerchin_and_responding.SearchInteractorImpl
import com.example.myplaylistmaker.domain.settings.SettingsInteractor
import com.example.myplaylistmaker.domain.settings.SettingsInteractImpl
import com.example.myplaylistmaker.domain.settings.ThemeSettings
import com.example.myplaylistmaker.domain.sharing.ExternalNavigator
import com.example.myplaylistmaker.domain.sharing.SharingInteractor
import com.example.myplaylistmaker.domain.sharing.SharingInteractorImpl


object Creator {

    private lateinit var application: App
    fun init(application: App) {
        this.application = application
    }

    //player
    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl()
    }

    fun providePlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    //search
    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(provideTracksRepository())
    }

    fun provideTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(application.applicationContext))
    }

    fun provideSearchHistory(): SearchHistory {
        return SearchHistoryImpl(application)
    }

    fun provideSearchHistoryInteractor (): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(provideSearchHistory())
    }


    //settings
    fun provideSettingsIneractor(): SettingsInteractor {
        return SettingsInteractImpl(provideThemeSettings())
    }

    fun provideThemeSettings(): ThemeSettings {
        return ThemeSettingsImpl(application)
    }

    //sharing
    fun provideSharingIneractor(): SharingInteractor {
        return SharingInteractorImpl(provideExternalNavigator())
    }

    fun provideExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(application)
    }



}