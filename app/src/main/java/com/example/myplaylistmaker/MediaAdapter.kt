package com.example.myplaylistmaker


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MediaAdapter() : RecyclerView.Adapter<MediaViewHolder> () {

    var media=ArrayList<MediaData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(media.get(position))
    }

    override fun getItemCount(): Int = media.size

}