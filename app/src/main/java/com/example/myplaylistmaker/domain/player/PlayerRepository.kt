package com.example.myplaylistmaker.domain.player

interface PlayerRepository {

    fun play()
    fun pause()
    fun destroy()
    fun preparePlayer(url: String, completion: () -> Unit)
    fun timeTransfer() :String
    fun playerStateReporter() : PlayerState
}