package com.example.myplaylistmaker.di.mediaLibraryModule

import com.example.myplaylistmaker.ui.mediaLibrary.viewModels.FavoritesViewModel
import com.example.myplaylistmaker.ui.mediaLibrary.viewModels.MediaLibraryViewModel
import com.example.myplaylistmaker.ui.mediaLibrary.viewModels.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryModule=module {
    viewModel { FavoritesViewModel() }
    viewModel { PlaylistViewModel() }
    viewModel { MediaLibraryViewModel() }
}