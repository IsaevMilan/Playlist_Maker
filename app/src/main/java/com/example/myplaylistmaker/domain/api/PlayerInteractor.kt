package com.example.myplaylistmaker.domain.api

import com.example.myplaylistmaker.domain.PlayerState

interface PlayerInteractor {
    fun play()
    fun pause()
    fun destroy()
    fun createPlayer(url: String, completion: ()->Unit)
    fun getTime(): String
    fun playerStateListener(): PlayerState
}