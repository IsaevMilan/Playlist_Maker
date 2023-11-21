package com.example.myplaylistmaker.di.search_module

import com.example.myplaylistmaker.ui.player.view_model.PlayerViewModel
import com.example.myplaylistmaker.ui.search.view_model_for_activity.SearchViewModel
import com.example.myplaylistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {

    viewModel {
        SearchViewModel(get(), get())
    }


    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        PlayerViewModel(get())
    }


}