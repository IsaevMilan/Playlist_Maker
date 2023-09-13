package com.example.myplaylistmaker.ui.player.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myplaylistmaker.creator.Creator
import com.example.myplaylistmaker.domain.player.PlayerInteractor
import com.example.myplaylistmaker.domain.player.PlayerState

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
): ViewModel(){

    fun createPlayer(url: String, completion: ()->Unit) {
        playerInteractor.createPlayer(url, completion)
    }
    fun play() {
        playerInteractor.play()
    }
    fun pause() {
        playerInteractor.pause()
    }
    fun destroy() {
        playerInteractor.destroy()
    }

    fun getTime(): String {
        return playerInteractor.getTime()
    }

    fun playerStateListener(): PlayerState {
        return playerInteractor.playerStateListener()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                // 1
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlayerViewModel(
                        Creator.providePlayerInteractor()
                    ) as T
                }
            }
    }
}