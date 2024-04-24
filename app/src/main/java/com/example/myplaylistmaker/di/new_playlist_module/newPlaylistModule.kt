package com.example.myplaylistmaker.di.new_playlist_module

import androidx.room.Room
import com.example.myplaylistmaker.data.converters.NewPlaylistConverter
import com.example.myplaylistmaker.data.db.NewPlaylistDatabase
import com.example.myplaylistmaker.data.db.TrackInPlaylistDatabase
import com.example.myplaylistmaker.data.newPlaylist.NewPlaylistRepositoryImpl
import com.example.myplaylistmaker.domain.playlist.PlaylistInteractor
import com.example.myplaylistmaker.domain.playlist.PlaylistInteractorImpl
import com.example.myplaylistmaker.domain.playlist.PlaylistRepository
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
    single {
        Room.databaseBuilder(androidContext(), TrackInPlaylistDatabase::class.java, "track_playlist_table")
            .allowMainThreadQueries()
            .build()
    }

    factory { NewPlaylistConverter() }

    single <PlaylistRepository> {
        NewPlaylistRepositoryImpl(get(),get(),get())
    }

    single <PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }

    viewModel {
        NewPlaylistViewModel(get(), get())
    }
}