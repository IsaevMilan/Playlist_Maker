package com.example.myplaylistmaker.ui.mediaLibrary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylistmaker.databinding.PlaylistItemBinding
import com.example.myplaylistmaker.domain.newPlaylist.NewPlaylist

class PlaylistAdapter (
    private var plalists: List<NewPlaylist>,
    private val clickListener: PlaylistClick
) :
RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(PlaylistItemBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return plalists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(plalists[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(plalists[position])
            //notifyDataSetChanged()
        }
    }

    fun interface PlaylistClick {
        fun onClick(playlist: NewPlaylist)
    }
}