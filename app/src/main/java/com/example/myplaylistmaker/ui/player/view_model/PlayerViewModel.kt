package com.example.myplaylistmaker.ui.player.view_model

import android.media.MediaPlayer
import android.util.Log
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

    var timeJob: Job?=null
    var stateLiveData = MutableLiveData<PlayerState>()
    var playTimer = MutableLiveData ("00:00")

    fun createPlayer(url: String) {
        playerInteractor.createPlayer(url,listener = object : PlayerStateListener {
            override fun onStateChanged(state: PlayerState) {
                stateLiveData.postValue(state)

            }

        })
   }

    fun play() {
        playerInteractor.play()
        timeJob?.start()
    }

    fun pause() {
        playerInteractor.pause()
    }

    fun destroy() {
        playerInteractor.destroy()
        timeJob?.cancel()
    }

        fun getTimeFromInteractor(): LiveData<String> {
            timeJob=viewModelScope.launch {
                while (true) {
                    delay(PLAYER_BUTTON_PRESSING_DELAY)
                    playerInteractor.getTime().collect() {
                        playTimer.postValue(it)
                    }
                }
            }
            return playTimer
        }

        fun putTime(): LiveData<String> {
            getTimeFromInteractor()
            playTimer.value?.let { Log.d("время в модели", it) }
            return playTimer
        }

        companion object {
            const val PLAYER_BUTTON_PRESSING_DELAY = 300L
        }
    }



