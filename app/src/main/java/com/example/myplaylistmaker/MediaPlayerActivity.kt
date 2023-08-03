package com.example.myplaylistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.util.concurrent.TimeUnit


class MediaPlayerActivity : AppCompatActivity() {

    private val mainHandler = Handler(Looper.getMainLooper())
    private var playerState = STATE_DEFAULT
    private lateinit var playButton: ImageButton
    private lateinit var pauseButton: ImageButton
    private var mediaPlayer = MediaPlayer()
    lateinit var timer: TextView

    private val timeFormatterRunnable = object : Runnable {
        override fun run() {
            val elapsedSeconds = TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.currentPosition.toLong())
            val seconds = elapsedSeconds % SECONDS_IN_MINUTE
            val minutes = elapsedSeconds / SECONDS_IN_MINUTE
            val formattedSeconds = String.format(FORMAT, seconds)
            val formattedMinutes = String.format(FORMAT, minutes)
            val formattedTime = "$formattedMinutes:$formattedSeconds"
            timer.text = formattedTime
            mainHandler.postDelayed(this, DELAY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)


        val playerTrackName = findViewById<TextView>(R.id.playerTrackName)
        val playerArtistName = findViewById<TextView>(R.id.playerArtistName)
        val trackTime = findViewById<TextView>(R.id.time)
        val album = findViewById<TextView>(R.id.album)
        val year = findViewById<TextView>(R.id.year)
        val genre = findViewById<TextView>(R.id.genre)
        val country = findViewById<TextView>(R.id.country)
        val cover = findViewById<ImageView>(R.id.trackCover)
        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)
        timer = findViewById(R.id.trackTimer)
        val backButton = findViewById<ImageView>(R.id.backArrow4)
        backButton.setOnClickListener {
            onBackPressed()
        }
        playButton.setOnClickListener { playbackControl() }
        pauseButton.setOnClickListener { playbackControl() }


        playerTrackName.text = intent.extras?.getString("Track Name") ?: "Unknown Track"
        playerArtistName.text = intent.extras?.getString("Artist Name") ?: "Unknown Artist"
        trackTime.text = intent.extras?.getString("Track Time") ?: "00:00"
        album.text = intent.extras?.getString("Album") ?: "Unknown Album"
        year.text = (intent.extras?.getString("Year") ?: "Year").take(4)
        genre.text = intent.extras?.getString("Genre") ?: "Unknown Genre"
        country.text = intent.extras?.getString("Country") ?: "Unknown Country"
        val getImage = (intent.extras?.getString("Cover") ?: "Unknown Cover").replace(
            "100x100bb.jpg",
            "512x512bb.jpg"
        )
        if (getImage != "Unknown Cover") {
            getImage.replace("100x100bb.jpg", "512x512bb.jpg")
            Glide.with(this)
                .load(getImage)
                .placeholder(R.drawable.placeholdermedia)
                .into(cover)
        }
        val url = intent.extras?.getString("URL")
        if (!url.isNullOrEmpty()) preparePlayer(url)
    }

    override fun onStop() {
        pausePlayer()
        super.onStop()
    }

    override fun onDestroy() {
        mainHandler.removeCallbacks(timeFormatterRunnable)
        mediaPlayer.release()
        super.onDestroy()
    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
            playButton.visibility = View.VISIBLE
            pauseButton.visibility = View.GONE

        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            playButton.visibility = View.VISIBLE
            pauseButton.visibility = View.GONE
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        playButton.visibility = View.GONE
        pauseButton.visibility = View.VISIBLE
        mainHandler.post(timeFormatterRunnable)
    }

    private fun pausePlayer() {
        mainHandler.removeCallbacks(timeFormatterRunnable)
        pauseButton.visibility = View.GONE
        playButton.visibility = View.VISIBLE
        playerState = STATE_PAUSED
        mediaPlayer.pause()
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val SECONDS_IN_MINUTE = 60
        private const val DELAY = 1000L
        private const val FORMAT = "%02d"
    }

}




     
