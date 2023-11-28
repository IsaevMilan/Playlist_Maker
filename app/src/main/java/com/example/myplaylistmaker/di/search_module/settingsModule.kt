package com.example.myplaylistmaker.di.search_module

import com.example.myplaylistmaker.data.settings.ThemeSettingsImpl
import com.example.myplaylistmaker.data.sharing.ExternalNavigatorImpl
import com.example.myplaylistmaker.domain.settings.SettingsInteractImpl
import com.example.myplaylistmaker.domain.settings.SettingsInteractor
import com.example.myplaylistmaker.domain.settings.ThemeSettings
import com.example.myplaylistmaker.domain.sharing.ExternalNavigator
import com.example.myplaylistmaker.domain.sharing.SharingInteractor
import com.example.myplaylistmaker.domain.sharing.SharingInteractorImpl
import com.example.myplaylistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule= module {

    single<ThemeSettings> {
        ThemeSettingsImpl(get(), get())
    }
    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }
    single<SettingsInteractor> {
        SettingsInteractImpl(get())
    }
    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }
    viewModel { SettingsViewModel(get(), get()) }

}