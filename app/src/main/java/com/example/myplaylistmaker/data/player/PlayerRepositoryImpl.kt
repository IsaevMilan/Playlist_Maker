package com.example.myplaylistmaker.data.player

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.util.Log
import com.example.myplaylistmaker.domain.player.PlayerRepository
import com.example.myplaylistmaker.domain.player.PlayerState
import com.example.myplaylistmaker.domain.player.PlayerStateListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : PlayerRepository {

    private var playerState = PlayerState.STATE_DEFAULT
    private lateinit var listener: PlayerStateListener
    private var trackTime = MutableStateFlow("00:00")
    private var playButtonJob: Job? = null
    override fun preparePlayer(url: String, listener: PlayerStateListener) {
        this.listener = listener
        if (playerState != PlayerState.STATE_DEFAULT) return
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
            listener.onStateChanged(playerState)
            playButtonJob?.start()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
            listener.onStateChanged(playerState)
        }
    }

    override fun play() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
        listener.onStateChanged(playerState)

    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
        listener.onStateChanged(playerState)
        Log.d("playerStateRep", playerState.toString())
    }

    override fun destroy() {
        mediaPlayer.release()
        playerState = PlayerState.STATE_DEFAULT
        listener.onStateChanged(playerState)
        playButtonJob?.cancel()
        Log.d("playerStateRep", playerState.toString())
    }

    @SuppressLint("SimpleDateFormat")
    override fun timing(): Flow<String> = flow {
        val sdf = SimpleDateFormat("mm:ss")
        while (true) {
            if ((playerState == PlayerState.STATE_PLAYING) or (playerState == PlayerState.STATE_PAUSED)) {
                emit(sdf.format(mediaPlayer.currentPosition))
            } else {
                emit("00:00")
            }
            delay(DELAY_MILLIS)
        }
    }

    override fun timeTransfer(): String {
        return trackTime.toString()
    }

    override fun playerStateReporter(): PlayerState {
        return playerState
    }

    companion object {
        const val DELAY_MILLIS = 300L
    }
}