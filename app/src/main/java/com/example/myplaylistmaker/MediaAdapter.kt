package com.example.myplaylistmaker

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MediaAdapter(
    private val media: List<MediaData>,
    private val searchHistory: SearchHistory
) : RecyclerView.Adapter<MediaViewHolder>() {


    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return MediaViewHolder(view)
    }


    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {


        App.getSharedPreferences()
        holder.bind(media[position])
        holder.itemView.setOnClickListener {

            val intent = Intent(holder.itemView.context, MediaPlayerActivity::class.java)

            intent.putExtra("Track Name", media[position].trackName)
            intent.putExtra("Artist Name", media[position].artistName)
            val trackTime = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(media[position].trackTimeMillis)
            intent.putExtra("Track Time", trackTime)
            intent.putExtra("Album", media[position].collectionName)
            intent.putExtra("Year", media[position].releaseDate)
            intent.putExtra("Genre", media[position].primaryGenreName)
            intent.putExtra("Country", media[position].country)
            intent.putExtra("Cover", media[position].artworkUrl100)
            intent.putExtra("URL", media[position].previewUrl)
            holder.itemView.context.startActivity(intent)

            searchHistory.editArray(media[position])
            notifyDataSetChanged()

        }
    }

    override fun getItemCount(): Int = media.size
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)

        }
        return current
    }
}

