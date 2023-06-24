package com.example.myplaylistmaker


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MediaAdapter(val clickListener: MediaClickListener) : RecyclerView.Adapter<MediaViewHolder> () {

    fun interface MediaClickListener {
        fun onTrackClick(track: MediaData)
    }
    var media = ArrayList<MediaData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(media[position])
        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int = media.size

}