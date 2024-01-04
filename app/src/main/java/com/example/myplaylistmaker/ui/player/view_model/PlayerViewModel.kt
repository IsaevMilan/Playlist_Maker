package com.example.myplaylistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylistmaker.domain.player.PlayerInteractor
import com.example.myplaylistmaker.domain.player.PlayerState
import com.example.myplaylistmaker.domain.player.PlayerStateListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    var timeJob: Job? = null
    var stateLiveData = MutableLiveData(PlayerState.STATE_DEFAULT)
    var playTimer = MutableLiveData("00:00")

    fun createPlayer(url: String) {
        playerInteractor.createPlayer(url, listener = object : PlayerStateListener {
            override fun onStateChanged(state: PlayerState) {
                stateLiveData.postValue(state)

            }

        })
    }

    fun play() {
        playerInteractor.play()
        /*timeJob?.start()*/
        timeJob = viewModelScope.launch {
            while (true) {
                playTimer.postValue(playerInteractor.getTime())
                delay(PLAYER_BUTTON_PRESSING_DELAY)


            }
        }
    }

    fun pause() {
        playerInteractor.pause()
        timeJob?.cancel()
    }

    fun destroy() {
        playerInteractor.destroy()
        timeJob?.cancel()
    }

    fun getTimeFromInteractor(): LiveData<String> {


        return playTimer
    }

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 300L
    }
}



