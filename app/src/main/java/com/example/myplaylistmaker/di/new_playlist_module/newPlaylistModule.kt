package com.example.myplaylistmaker.di.new_playlist_module

import androidx.room.Room
import com.example.myplaylistmaker.data.converters.NewPlaylistConverter
import com.example.myplaylistmaker.data.db.NewPlaylistDatabase
import com.example.myplaylistmaker.data.newPlaylist.NewPlaylistRepositoryImpl
import com.example.myplaylistmaker.domain.newPlaylist.NewPlaylistInteractor
import com.example.myplaylistmaker.domain.newPlaylist.NewPlaylistInteractorImpl
import com.example.myplaylistmaker.domain.newPlaylist.NewPlaylistRepository
import com.example.myplaylistmaker.ui.mediaLibrary.viewModels.NewPlaylistViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newPlaylistModule = module {
    single {
        Room.databaseBuilder(androidContext(), NewPlaylistDatabase::class.java, "playlist_table")
            .allowMainThreadQueries()
            .build()
    }

    factory { NewPlaylistConverter() }

    single <NewPlaylistRepository> {
        NewPlaylistRepositoryImpl(get(),get(),get())
    }

    single <NewPlaylistInteractor> {
        NewPlaylistInteractorImpl(get())
    }

    viewModel {
        NewPlaylistViewModel(get(), get())
    }
}