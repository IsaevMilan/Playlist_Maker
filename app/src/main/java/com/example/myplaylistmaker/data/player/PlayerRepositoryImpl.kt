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
import java.util.Locale

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : PlayerRepository {

    private var playerState = PlayerState.STATE_DEFAULT
    private lateinit var listener: PlayerStateListener
    private var trackTime = MutableStateFlow("00:00")

    override fun preparePlayer(url: String, listener: PlayerStateListener) {
        this.listener = listener
        if (playerState != PlayerState.STATE_DEFAULT) return
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
            listener.onStateChanged(playerState)

        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
            listener.onStateChanged(playerState)
        }
    }

    override fun play() {
        if (playerState != PlayerState.STATE_DEFAULT) {
            mediaPlayer.start()
            playerState = PlayerState.STATE_PLAYING
            listener.onStateChanged(playerState)
        }
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
        Log.d("playerStateRep", playerState.toString())
    }


    override fun timing(): String {
        val sdf = SimpleDateFormat("mm:ss", Locale.getDefault())

        if ((playerState == PlayerState.STATE_PLAYING) or (playerState == PlayerState.STATE_PAUSED)) {
            return (sdf.format(mediaPlayer.currentPosition))
        } else {
            return ("00:00")
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