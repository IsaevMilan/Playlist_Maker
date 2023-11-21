package com.example.myplaylistmaker.ui.player.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.ActivityMediaPlayerBinding
import com.example.myplaylistmaker.domain.player.PlayerState
import com.example.myplaylistmaker.domain.search.models.Track
import com.example.myplaylistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerActivity : AppCompatActivity() {

    private var mainThreadHandler: Handler? = Handler(Looper.getMainLooper())
    private lateinit var playerState: PlayerState
    val PlayerViewModel by viewModel<PlayerViewModel> ()
    private lateinit var binding: ActivityMediaPlayerBinding
    private var url=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding=ActivityMediaPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //вью-модель


        binding.playButton.isEnabled = false

        playerState = PlayerState.STATE_PAUSED
        mainThreadHandler = Handler(Looper.getMainLooper())
        binding.backArrow4.setOnClickListener {
            finish()
        }
        val track = intent.getParcelableExtra<Track>("track")

        binding.playerTrackName.text = track?.trackName ?: "Unknown Track"
        binding.playerArtistName.text = track?.artistName ?: "Unknown Artist"
        binding.time.text = track?.trackTimeMillis ?: "00:00"
        binding.album.text = track?.collectionName?: "Unknown Album"
        binding.year.text = (track?.releaseDate ?: "Year").take(4)
        binding.genre.text = track?.primaryGenreName ?: "Unknown Genre"
        binding.country.text = track?.country ?: "Unknown Country"
        val getImage = (track?.artworkUrl100 ?: "Unknown Cover").replace(
            "100x100bb.jpg",
            "512x512bb.jpg"
        )
        if (getImage != "Unknown Cover") {
            getImage.replace("100x100bb.jpg", "512x512bb.jpg")
            Glide.with(this)
                .load(getImage)
                .placeholder(R.drawable.placeholdermedia)
                .into(binding.trackCover)
        }
        url = track?.previewUrl ?: return


        PlayerViewModel.createPlayer(url) {
            preparePlayer()
        }

        binding.playButton.setOnClickListener {
            PlayerViewModel.play()
        }
        binding.pauseButton.setOnClickListener {
            PlayerViewModel.pause()
        }
        mainThreadHandler?.post(
            updateButton()
        )
        mainThreadHandler?.post(
            updateTimer()
        )

    }

    override fun onPause (){
        super.onPause()
        PlayerViewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        PlayerViewModel.destroy()
    }

    fun preparePlayer() {
        binding.playButton.isEnabled = true
        binding.playButton.visibility = View.VISIBLE
        binding.pauseButton.visibility = View.GONE
    }

    fun playerStateDrawer() {
        playerState = PlayerViewModel.playerStateListener()
        when (playerState) {
            PlayerState.STATE_DEFAULT -> {
                binding.playButton.visibility = View.VISIBLE
                binding.playButton.alpha = 0.5f
                binding.pauseButton.visibility = View.GONE
            }

            PlayerState.STATE_PREPARED -> {
                binding.playButton.visibility = View.VISIBLE
                binding.playButton.alpha = 1f
                binding.pauseButton.visibility = View.GONE
            }

            PlayerState.STATE_PAUSED -> {
                binding.playButton.visibility = View.VISIBLE
                binding.playButton.alpha = 1f
                binding.pauseButton.visibility = View.GONE
            }

            PlayerState.STATE_PLAYING -> {
                binding.pauseButton.visibility = View.VISIBLE
                binding.playButton.visibility = View.GONE
            }
        }
    }

    private fun updateButton(): Runnable {
        val updatedButton = Runnable {
            playerStateDrawer()
            mainThreadHandler?.postDelayed(updateButton(), PLAYER_BUTTON_PRESSING_DELAY)
        }
        return updatedButton
    }

    private fun updateTimer(): Runnable {
        val updatedTimer = Runnable {
            binding.trackTimer.text = PlayerViewModel.getTime()
            mainThreadHandler?.postDelayed(updateTimer(), PLAYER_BUTTON_PRESSING_DELAY)
        }
        return updatedTimer
    }

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 100L
    }
}



     
