package com.example.myplaylistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylistmaker.domain.db.FavoritesInteractor
import com.example.myplaylistmaker.domain.player.PlayerInteractor
import com.example.myplaylistmaker.domain.player.PlayerState
import com.example.myplaylistmaker.domain.player.PlayerStateListener
import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favouritesInteractor: FavoritesInteractor
) : ViewModel() {

    var timeJob: Job? = null
    private val stateLiveData = MutableLiveData(PlayerState.STATE_DEFAULT)
    private val playTimer = MutableLiveData("00:00")
    private val favouritesIndicator = MutableLiveData<Boolean>()
    var favouritesJob:Job?=null

    fun getStateLiveData(): LiveData<PlayerState> {
        return stateLiveData
    }

   /* fun getPlayTimerLiveData(): LiveData<String> {
        return playTimer
    }

    fun getFavouritesIndicatorLiveData(): LiveData<Boolean> {
        return favouritesIndicator
    }*/

    fun createPlayer(url: String) {
        playerInteractor.createPlayer(url, listener = object : PlayerStateListener {
            override fun onStateChanged(state: PlayerState) {
                stateLiveData.postValue(state)

            }

        })
    }

    fun play() {
        timeJob?.cancel()
        playerInteractor.play()
        timeJob = viewModelScope.launch {
            while (true) {
                playTimer.postValue(playerInteractor.getTime())
                delay(PLAYER_BUTTON_PRESSING_DELAY_MILLIS)


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

    fun onFavoriteClicked(track: Track) {
        if (track.isFavorite) {
            track.trackId?.let { favouritesInteractor.favouritesDelete(track) }
        } else track.trackId?.let {
            favouritesInteractor.favouritesAdd(
                track
            )
        }
    }

    fun cliclFavourites (track: Track) : LiveData<Boolean> {

        favouritesJob=viewModelScope.launch{

            while (true) {
                delay(PLAYER_BUTTON_PRESSING_DELAY_MILLIS)
                track.trackId?.let { id ->
                    favouritesInteractor.favouritesCheck(id)
                        .collect {value ->
                          favouritesIndicator.postValue(value)
                        }
                }
            }
        }
        return favouritesIndicator
    }

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY_MILLIS = 200L
    }
}



