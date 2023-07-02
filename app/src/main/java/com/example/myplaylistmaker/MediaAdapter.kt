package com.example.myplaylistmaker

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MediaAdapter( var media: ArrayList<MediaData>) : RecyclerView.Adapter<MediaViewHolder> () {

    private val searchActivityObj = SearchActivity()
    fun interface MediaClickListener {
        fun onTrackClick(track: MediaData)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return MediaViewHolder(view)
    }
    var searchHistory = ArrayList<MediaData>()

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {

//        if (media.isNotEmpty()) {
//            holder.bind(media[position])
//            holder.bind(searchHistory[position])
//            holder.itemView.setOnClickListener {
//               // media.onTrackClick(media[position])
//               // media.onTrackClick(searchHistory[position])
//            }
//        }

        App.getSharedPreferences()
        holder.bind(media[position])
        holder.itemView.setOnClickListener {

            val intent = Intent(holder.itemView.context, MediaActivity::class.java)
            intent.putExtra("Track Name", media[position].trackName)
            intent.putExtra("Artist Name", media[position].artistName)
            val trackTime = SimpleDateFormat("mm:ss",
                Locale.getDefault()).format(media[position].trackTimeMillis)
            intent.putExtra("Track Time", trackTime)
            intent.putExtra("Album", media[position].collectionName)
            intent.putExtra("Year", media[position].releaseDate)
            intent.putExtra("Genre", media[position].primaryGenreName)
            intent.putExtra("Country", media[position].country)
            intent.putExtra("Cover", media[position].artworkUrl100)
            holder.itemView.context.startActivity(intent)

            searchActivityObj.searchHistoryObj.editArray(media[position])
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = media.size

}

