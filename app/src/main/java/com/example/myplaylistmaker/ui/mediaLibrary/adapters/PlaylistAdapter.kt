package com.example.myplaylistmaker.ui.mediaLibrary.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylistmaker.databinding.PlaylistItemBinding
import com.example.myplaylistmaker.domain.playlist.Playlist

class PlaylistAdapter(
    private var playlists: List<Playlist> = emptyList(),
    private val clickListener: PlaylistClick
) :

    RecyclerView.Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(PlaylistItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(playlists[position])
            notifyDataSetChanged()
        }
    }
    fun setItems(items: List<Playlist>) {
        playlists = items
        notifyDataSetChanged()
    }

    fun interface PlaylistClick {
        fun onClick(playlist: Playlist)
    }

}
