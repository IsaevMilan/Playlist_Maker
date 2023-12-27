package com.example.myplaylistmaker.ui.player.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myplaylistmaker.domain.player.PlayerInteractor
import com.example.myplaylistmaker.domain.player.PlayerState
import com.example.myplaylistmaker.domain.player.PlayerStateListener

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    var stateLiveData = MutableLiveData<PlayerState>()

    fun createPlayer(url: String) {
        playerInteractor.createPlayer(url,listener = object : PlayerStateListener {
            override fun onStateChanged(state: PlayerState) {
                stateLiveData.postValue(state)
                Log.d ("playerStateModel", stateLiveData.value.toString())
            }

        })
        Log.d ("playerStateModel", stateLiveData.value.toString())
    }

    fun play() {
        playerInteractor.play()
        Log.d ("playerStateModel", stateLiveData.value.toString())
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



}
