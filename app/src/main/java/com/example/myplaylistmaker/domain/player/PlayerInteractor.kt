package com.example.myplaylistmaker.domain.player

import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {
    fun play()
    fun pause()
    fun destroy()
    fun createPlayer(url: String, listener: PlayerStateListener)
    fun getTime(): String
    fun playerStateListener(): PlayerState
}