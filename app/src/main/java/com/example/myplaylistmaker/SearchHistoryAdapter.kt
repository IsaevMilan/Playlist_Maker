package com.example.myplaylistmaker


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.NonDisposableHandle.parent


class SearchHistoryAdapter(val clickListener: MediaAdapter.MediaClickListener) : RecyclerView.Adapter<MediaViewHolder> () {

    var searchHistory = ArrayList<MediaData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(searchHistory[position])
        holder.itemView.setOnClickListener{

        }
    }

    override fun getItemCount(): Int = searchHistory.size
}