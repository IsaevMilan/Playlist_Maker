package com.example.myplaylistmaker.ui.search.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.TrackItemBinding
import com.example.myplaylistmaker.domain.search.models.Track

class TrackViewHolder(private val binding:TrackItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Track) {
        binding.trackName.text = item.trackName
        binding.trackAuthor.text = item.artistName
        binding.trackTime.text = item.trackTimeMillis
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholdermedia)
            .into(binding.trackImage)
    }

}