package com.example.myplaylistmaker.ui.player.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.ActivityMediaPlayerBinding
import com.example.myplaylistmaker.domain.player.PlayerState
import com.example.myplaylistmaker.domain.search.models.Track
import com.example.myplaylistmaker.ui.player.view_model.PlayerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerActivity : AppCompatActivity() {

    //    private var mainThreadHandler: Handler? = Handler(Looper.getMainLooper())
    private val playerViewModel by viewModel<PlayerViewModel>()
    private lateinit var binding: ActivityMediaPlayerBinding
    private var url = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        binding = ActivityMediaPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  binding.playButton.isEnabled = false

//        mainThreadHandler = Handler(Looper.getMainLooper())
        binding.backArrow4.setOnClickListener {
            finish()
        }
        val track = intent.getParcelableExtra<Track>("track")

        binding.playerTrackName.text = track?.trackName ?: "Unknown Track"
        binding.playerArtistName.text = track?.artistName ?: "Unknown Artist"
        binding.time.text = track?.trackTimeMillis ?: "00:00"
        binding.album.text = track?.collectionName ?: "Unknown Album"
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


        playerViewModel.createPlayer(url)

        binding.playButton.setOnClickListener {
            if (playerViewModel.stateLiveData.value == PlayerState.STATE_PLAYING)
                playerViewModel.pause() else playerViewModel.play()
        }

        updateButton()

        playerViewModel.getTimeFromInteractor().observe(this) { timer ->
            binding.trackTimer.text = timer
            Log.d("время в активити", timer)
        }

//        binding.pauseButton.setOnClickListener {
//            playerViewModel.pause()
//        }

//        mainThreadHandler?.post(
//            updateButton()
//        )
//
//
//        mainThreadHandler?.post(
//            updateTimer()
//        )

    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.destroy()
    }

    private fun preparePlayer() {
        binding.playButton.isEnabled = true
        binding.playButton.visibility = View.VISIBLE
        binding.pauseButton.visibility = View.GONE
    }

    @SuppressLint("ResourceType")
    fun playerStateDrawer() {
        playerViewModel.stateLiveData.observe(this) {
            when (playerViewModel.stateLiveData.value) {
                PlayerState.STATE_DEFAULT -> {
                    binding.playButton.isEnabled=false
                    binding.playButton.setImageResource(R.drawable.buttonplay)
                    binding.playButton.alpha = 0.5f
                }


                PlayerState.STATE_PREPARED -> {
                    preparePlayer()
                    binding.playButton.setImageResource(R.drawable.buttonplay)
                    binding.playButton.alpha = 1f
                }

                PlayerState.STATE_PLAYING -> {
                    binding.playButton.setImageResource(R.drawable.pause_button)

                }

                PlayerState.STATE_PAUSED -> {
                    binding.playButton.setImageResource(R.drawable.buttonplay)
                    binding.playButton.alpha = 1f
                }

                else -> {

                }
            }
        }
    }

    private fun updateButton() {

        lifecycleScope.launch {
            delay(PLAYER_BUTTON_PRESSING_DELAY)
            playerStateDrawer()

        }
    }

//    private var buttonClickJob:Job?=null
//    private fun clickDebounce (action:() -> Unit) {
//        if (buttonClickJob?.isActive == true) return
//        buttonClickJob = lyfecycleScope.launch {
//            action.invoke()
//            delay (PLAYER_BUTTON_PRESSING_DELAY)
//        }
//    }

//    private fun updateTimer(): Runnable {
//        val updatedTimer = Runnable {
//            binding.trackTimer.text = playerViewModel.getTime()
////            mainThreadHandler?.postDelayed(updateTimer(), PLAYER_BUTTON_PRESSING_DELAY)
//        }
//        return updatedTimer
//    }

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 300L
    }
}



     
