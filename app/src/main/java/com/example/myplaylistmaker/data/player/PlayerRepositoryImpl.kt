package com.example.myplaylistmaker.data.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.myplaylistmaker.domain.player.PlayerRepository
import com.example.myplaylistmaker.domain.player.PlayerState
import com.example.myplaylistmaker.domain.player.PlayerStateListener
import java.text.SimpleDateFormat

class  PlayerRepositoryImpl(private val mediaPlayer:MediaPlayer) : PlayerRepository {

    private var playerState = PlayerState.STATE_DEFAULT
    var timePlayed = "00:00"
    private var mainThreadHandler: Handler? = Handler(Looper.getMainLooper())
    private lateinit var listener: PlayerStateListener
    override fun preparePlayer(url: String, listener: PlayerStateListener) {
        this.listener = listener
        if (playerState != PlayerState.STATE_DEFAULT) return
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
            listener.onStateChanged(playerState)
            Log.d ("playerStateRep", playerState.toString())
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
            listener.onStateChanged(playerState)
        }
    }

    override fun play() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
        mainThreadHandler?.post(
            timing()
        )

        listener.onStateChanged(playerState)
        Log.d ("playerStateRep", playerState.toString())
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
        listener.onStateChanged(playerState)
        Log.d ("playerStateRep", playerState.toString())
    }

    override fun destroy() {
        mediaPlayer.release()
        playerState = PlayerState.STATE_DEFAULT
        listener.onStateChanged(playerState)
        Log.d ("playerStateRep", playerState.toString())
    }
    private fun timing(): Runnable {
        return object : Runnable {
            override fun run() {
                if ((playerState == PlayerState.STATE_PLAYING) or (playerState == PlayerState.STATE_PAUSED)) {
                    val sdf = SimpleDateFormat("mm:ss")
                    timePlayed = sdf.format(mediaPlayer.currentPosition)
                    mainThreadHandler?.postDelayed(this, DELAY_MILLIS)
                } else {
                    timePlayed = "00:00"
                    mainThreadHandler?.postDelayed(this, DELAY_MILLIS)
                }
            }
        }
    }
    override fun timeTransfer(): String {
        return timePlayed
    }
    override fun playerStateReporter(): PlayerState {
        return playerState
    }
    companion object {
        const val DELAY_MILLIS = 100L
    }
}