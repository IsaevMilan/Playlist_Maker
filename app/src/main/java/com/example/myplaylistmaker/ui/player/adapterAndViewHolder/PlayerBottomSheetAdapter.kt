package com.example.myplaylistmaker.ui.player.adapterAndViewHolder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylistmaker.databinding.NewPlaylistItemBinding
import com.example.myplaylistmaker.domain.playlist.Playlist

class PlayerBottomSheetAdapter(
    private var playlists: List<Playlist>,
    private val clickListener: PlaylistClick
) : RecyclerView.Adapter<PlayerBottomSheetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerBottomSheetViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlayerBottomSheetViewHolder(NewPlaylistItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlayerBottomSheetViewHolder, position: Int) {

        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(playlists[position])
            notifyDataSetChanged()
        }
    }

    fun interface PlaylistClick {
        fun onClick(playlist: Playlist)
    }

    fun setItems(items: List<Playlist>) {
        playlists = items
        notifyDataSetChanged()
    }
}

