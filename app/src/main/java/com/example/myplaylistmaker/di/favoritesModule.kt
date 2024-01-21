package com.example.myplaylistmaker.di

import androidx.room.Room
import com.example.myplaylistmaker.data.FavouritesRepositoryImpl
import com.example.myplaylistmaker.data.converters.TrackConvertor
import com.example.myplaylistmaker.data.db.AppDatabase
import com.example.myplaylistmaker.domain.db.FavoritesInteractor
import com.example.myplaylistmaker.domain.db.FavoritesInteractorImpl
import com.example.myplaylistmaker.domain.db.FavouritesRepository
import com.example.myplaylistmaker.ui.mediaLibrary.viewModels.FavouritesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favouritesModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app_database")
            .allowMainThreadQueries()
            .build()
    }

    factory { TrackConvertor() }


    single<FavouritesRepository>
    { FavouritesRepositoryImpl(get(), get()) }

    single <FavoritesInteractor> { FavoritesInteractorImpl(get()) }

    viewModel{
        FavouritesViewModel(get(), get())
    }
}