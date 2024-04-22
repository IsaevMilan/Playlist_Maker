package com.example.myplaylistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myplaylistmaker.domain.db.FavoritesInteractor
import com.example.myplaylistmaker.domain.player.PlayerInteractor
import com.example.myplaylistmaker.domain.player.PlayerState
import com.example.myplaylistmaker.domain.player.PlayerStateListener
import com.example.myplaylistmaker.domain.playlist.Playlist
import com.example.myplaylistmaker.domain.playlist.PlaylistInteractor
import com.example.myplaylistmaker.domain.search.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favouritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private var timeJob: Job? = null
    private var favoritesJob: Job? = null
    private val stateLiveData = MutableLiveData(PlayerState.STATE_DEFAULT)
    private val isFavoriteLiveData = MutableLiveData<Boolean>()
    private val playTimer = MutableLiveData("00:00")
    val myPlaylist: MutableLiveData<List<Playlist>> = MutableLiveData<List<Playlist>>(emptyList())

    fun stateLiveData(): LiveData<PlayerState> = stateLiveData
    fun isFavoriteLiveData(): LiveData<Boolean> = isFavoriteLiveData

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

    fun onFavoriteClicked(track: Track?) {
        track ?: return
        favoritesJob?.cancel()
        favoritesJob = viewModelScope.launch {
            if (track.isFavorite) {
                favouritesInteractor.favouritesDelete(track)
            } else {
                favouritesInteractor.favouritesAdd(track)
            }
            isFavoriteLiveData.value = track.isFavorite
        }
    }

    fun playlistMaker(): LiveData<List<Playlist>> {
        viewModelScope.launch {
            playlistInteractor.queryPlaylist()
                .collect {
                    if (it.isNotEmpty()) {
                        myPlaylist.postValue(it)
                    } else {
                        myPlaylist.postValue(emptyList())
                    }
                }
        }
        return myPlaylist
    }

    val playlistAdding = MutableLiveData(false)

    fun addTrack(track: Track, playlist: Playlist) {
        if (playlist.trackArray.contains(track.trackId)) {
            playlistAdding.postValue(true)


        } else {
            playlistAdding.postValue(false)
            playlist.trackArray = (playlist.trackArray + track.trackId)!!
            playlist.arrayNumber = (playlist.arrayNumber?.plus(1))!!
            playlistInteractor.update(track, playlist)

        }
    }

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY_MILLIS = 200L
    }
}

