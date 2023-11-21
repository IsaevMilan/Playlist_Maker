package com.example.myplaylistmaker.di.player_module

import com.example.myplaylistmaker.data.player.PlayerRepositoryImpl
import com.example.myplaylistmaker.domain.player.PlayerInteractor
import com.example.myplaylistmaker.domain.player.PlayerInteractorImpl
import com.example.myplaylistmaker.domain.player.PlayerRepository
import com.example.myplaylistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val PlayerModule = module {
    factory <PlayerRepository> {
        PlayerRepositoryImpl ()
    }

    factory <PlayerInteractor> {
        PlayerInteractorImpl (get())
    }

}