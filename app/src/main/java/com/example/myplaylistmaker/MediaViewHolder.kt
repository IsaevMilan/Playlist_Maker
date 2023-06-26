package com.example.myplaylistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*

class MediaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackImage: ImageView = itemView.findViewById(R.id.trackImage)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val trackAuthor: TextView = itemView.findViewById(R.id.trackAuthor)
    private val tracklength: TextView = itemView.findViewById(R.id.track_time)
    private var trackNumber:String = ""

    fun bind(item: MediaData) {
        trackName.text = item.trackName
        trackAuthor.text = item.artistName
        tracklength.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
        trackNumber= item.mediaId

        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(trackImage)

    }
}