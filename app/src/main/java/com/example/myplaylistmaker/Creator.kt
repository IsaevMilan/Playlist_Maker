package com.example.myplaylistmaker

import com.example.myplaylistmaker.data.dto.PlayerRepositoryImpl
import com.example.myplaylistmaker.domain.api.PlayerInteractor
import com.example.myplaylistmaker.domain.api.PlayerRepository
import com.example.myplaylistmaker.domain.impl.PlayerInteractorImpl

object Creator {
    fun providePlayerInteractor(): PlayerInteractor {
    return PlayerInteractorImpl()
}
    fun providePlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }
}