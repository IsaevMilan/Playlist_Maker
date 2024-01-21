package com.example.myplaylistmaker.ui.player.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.ActivityMediaPlayerBinding
import com.example.myplaylistmaker.domain.player.PlayerState
import com.example.myplaylistmaker.domain.search.models.Track
import com.example.myplaylistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerActivity : AppCompatActivity() {

    private val playerViewModel by viewModel<PlayerViewModel>()
    private lateinit var binding: ActivityMediaPlayerBinding
    private var url = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        binding = ActivityMediaPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val radius = 8
        if (getImage != "Unknown Cover") {
            getImage.replace("100x100bb.jpg", "512x512bb.jpg")
            Glide.with(this)
                .load(getImage)
                .placeholder(R.drawable.placeholdermedia)
                .transform(RoundedCorners(radius))
                .into(binding.trackCover)
        }
        url = track?.previewUrl ?: return


        playerViewModel.createPlayer(url)

        binding.playButton.setOnClickListener {
            if (playerViewModel.stateLiveData.value == PlayerState.STATE_PLAYING)
                playerViewModel.pause() else playerViewModel.play()
        }

        playerStateDrawer()

        playerViewModel.getTimeFromInteractor().observe(this) { timer ->
            binding.trackTimer.text = timer
            Log.d("время в активити", timer)
        }

        //нажатие на кнопку нравится
        binding.favorites.setOnClickListener {
            playerViewModel.onFavoriteClicked(track)
        }

        playerViewModel.favouritesChecker(track).observe(this) { favourtitesIndicator ->
            if (favourtitesIndicator) {
                binding.favorites.setImageResource(R.drawable.like_button)
            } else binding.favorites.setImageResource(
                R.drawable.buttonhert
            )
        }

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

    fun playerStateDrawer() {
        playerViewModel.stateLiveData.observe(this) {
            when (playerViewModel.stateLiveData.value) {
                PlayerState.STATE_DEFAULT -> {
                    binding.playButton.setImageResource(R.drawable.buttonplay)

                }


                PlayerState.STATE_PREPARED -> {
                    preparePlayer()
                    binding.playButton.setImageResource(R.drawable.buttonplay)

                }

                PlayerState.STATE_PLAYING -> {
                    binding.playButton.setImageResource(R.drawable.pause_button)

                }

                PlayerState.STATE_PAUSED -> {
                    binding.playButton.setImageResource(R.drawable.buttonplay)

                }

                else -> {

                }
            }
        }
    }

    companion object {
        const val PLAYER_BUTTON_PRESSING_DELAY = 300L
    }
}



     
