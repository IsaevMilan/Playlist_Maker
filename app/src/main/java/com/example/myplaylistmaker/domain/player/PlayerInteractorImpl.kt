package com.example.myplaylistmaker.domain.player


class PlayerInteractorImpl (private val repository: PlayerRepository): PlayerInteractor {

    override fun play() {
        repository.play()
    }

    override fun pause() {
        repository.pause()
    }

    override fun destroy() {
        repository.destroy()
    }

    override fun createPlayer(url: String, completion: () -> Unit) {
        repository.preparePlayer(url, completion)
    }

    override fun getTime(): String {
        return repository.timeTransfer()
    }

    override fun playerStateListener(): PlayerState {
        return repository.playerStateReporter()
    }
}
