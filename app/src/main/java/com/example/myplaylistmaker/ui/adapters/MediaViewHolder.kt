package com.example.myplaylistmaker.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.domain.search.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewHolder(
    itemView: View,
    onItemClicked: (Track) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val trackAuthor: TextView = itemView.findViewById(R.id.trackAuthor)
    private val tracklength: TextView = itemView.findViewById(R.id.time)
    private var track: Track? = null

    init {
        itemView.setOnClickListener {
            val nonNullTrack = track ?: return@setOnClickListener
            onItemClicked.invoke(nonNullTrack)
        }
    }

    fun bind(item: Track) {
        this.track = item
        trackName.text = item.trackName
        trackAuthor.text = item.artistName
        tracklength.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(trackImage)
    }
}