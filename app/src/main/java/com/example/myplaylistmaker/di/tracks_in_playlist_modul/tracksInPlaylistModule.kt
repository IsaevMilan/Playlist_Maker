package com.example.myplaylistmaker.di.tracks_in_playlist_modul

import com.example.myplaylistmaker.data.tracks_In_playlist.TracksInPlaylistRepositoryImpl
import com.example.myplaylistmaker.domain.tracks_in_playlist.TracksInPlaylistInteractor
import com.example.myplaylistmaker.domain.tracks_in_playlist.TracksInPlaylistInteractorImpl
import com.example.myplaylistmaker.domain.tracks_in_playlist.TracksInPlaylistRepository
import com.example.myplaylistmaker.ui.tracks_in_playlist.viewModels.PlayListEditorViewModel
import com.example.myplaylistmaker.ui.tracks_in_playlist.viewModels.TracksInPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val tracksInPlaylistModule = module {

    single<TracksInPlaylistInteractor> { TracksInPlaylistInteractorImpl(get()) }
    single<TracksInPlaylistRepository> { TracksInPlaylistRepositoryImpl(get()) }
    viewModel { TracksInPlaylistViewModel(get(), get(), get()) }
    viewModel { PlayListEditorViewModel(get()) }
}