package com.example.myplaylistmaker


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class SearchHistoryAdapter() : RecyclerView.Adapter<MediaViewHolder> () {

    var searchHistory = ArrayList<MediaData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder =
        MediaViewHolder(parent)


    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(searchHistory[position])
    }

    override fun getItemCount(): Int = searchHistory.size
}