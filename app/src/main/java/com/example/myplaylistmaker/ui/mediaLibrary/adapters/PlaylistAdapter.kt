package com.example.myplaylistmaker.ui.mediaLibrary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myplaylistmaker.domain.newPlaylist.NewPlaylist
import com.example.myplaylistmaker.ui.mediaLibrary.fragments.FavoritesFragment
import com.example.myplaylistmaker.ui.mediaLibrary.fragments.PlaylistFragment

class PlaylistAdapter (
    private var plalists: List<NewPlaylist>,
    private val clickListener: PlaylistClick
) :
RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(PlaylistLayoutBinding.inflate(layoutInspector, parent, false))
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