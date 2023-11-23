package com.example.myplaylistmaker.di.player_module

import android.media.MediaPlayer
import com.example.myplaylistmaker.data.player.PlayerRepositoryImpl
import com.example.myplaylistmaker.domain.player.PlayerInteractor
import com.example.myplaylistmaker.domain.player.PlayerInteractorImpl
import com.example.myplaylistmaker.domain.player.PlayerRepository
import com.example.myplaylistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val PlayerModule = module {
    factory <PlayerRepository> {
        PlayerRepositoryImpl (get())
    }

    factory <PlayerInteractor> {
        PlayerInteractorImpl (get())
    }


    viewModel {PlayerViewModel(get())}

}