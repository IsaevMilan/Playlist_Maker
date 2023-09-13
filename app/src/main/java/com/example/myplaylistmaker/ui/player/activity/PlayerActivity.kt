package com.example.myplaylistmaker.ui.player.activity

import android.content.Context
import android.content.Intent
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
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.domain.search.models.Track
import java.util.concurrent.TimeUnit


class PlayerActivity : AppCompatActivity() {

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

        /*
            intent.putExtra("URL", media[position].previewUrl)
         */

        val track = intent.extras?.getSerializable(KEY_TRACK_EXTRA) as? Track
        playerTrackName.text = track?.trackName ?: "Unknown Track"
        playerArtistName.text = track?.artistName ?: "Unknown Artist"
        trackTime.text = track?.trackTimeMillis ?: "00:00"
        album.text = track?.collectionName ?: "Unknown Album"
        year.text = (track?.releaseDate ?: "Year").take(4)
        genre.text = track?.primaryGenreName ?: "Unknown Genre"
        country.text = track?.country ?: "Unknown Country"
        val getImage = (track?.artworkUrl100 ?: "Unknown Cover").replace(
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
        track?.previewUrl?.let(this::preparePlayer)
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

        private const val KEY_TRACK_EXTRA = "com.example.myplaylistmaker.TrackExtra"

        fun startActivity(context: Context, payload: Track) {
            val intent = Intent(context, PlayerActivity::class.java)
                .putExtra(KEY_TRACK_EXTRA, payload)
            context.startActivity(intent)
        }
    }
}




     
