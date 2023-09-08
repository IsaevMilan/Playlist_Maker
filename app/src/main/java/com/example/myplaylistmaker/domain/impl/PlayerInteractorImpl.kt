package com.example.myplaylistmaker.domain.impl

import com.example.myplaylistmaker.Creator
import com.example.myplaylistmaker.domain.PlayerState
import com.example.myplaylistmaker.domain.api.PlayerInteractor

class PlayerInteractorImpl : PlayerInteractor {
    var repository= Creator.providePlayerRepository()


    override fun play() {
        repository.play()
    }

    override fun pause() {
        repository.pause()
    }

    override fun destroy() {
        repository.destroy()
    }

    override fun createPlayer(url: String, completion: ()->Unit) {
        repository.preparePlayer(url, completion)
    }

    override fun getTime(): String {
        return repository.timeTransfer()
    }
    override fun playerStateListener(): PlayerState {
        return repository.playerStateReporter()
    }

}
