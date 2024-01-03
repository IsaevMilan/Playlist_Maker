package com.example.myplaylistmaker.domain.player

import kotlinx.coroutines.flow.Flow

interface PlayerRepository {

    fun play()
    fun pause()
    fun destroy()
    fun preparePlayer(url: String, listener:PlayerStateListener)
    fun timeTransfer() :String
    fun playerStateReporter() : PlayerState
    fun timing(): String
}