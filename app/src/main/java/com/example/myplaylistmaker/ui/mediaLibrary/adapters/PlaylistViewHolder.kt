package com.example.myplaylistmaker.ui.mediaLibrary.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.domain.newPlaylist.NewPlaylist

class PlaylistViewHolder(private val binding: PlaylistLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: NewPlaylist) {
        binding.playlistlittleName.text = item.playlistName
        val innerNumber = item.arrayNumber.toString()
        val text = if (innerNumber.toInt() % 10 == 1 && innerNumber.toInt() % 100 != 11) {
            " трек"
        } else if (innerNumber.toInt() % 10 == 2 || innerNumber.toInt() % 10 == 3 || innerNumber.toInt() % 10 == 4) {
            " трека"
        } else {
            " треков"
        }
        val number = "$innerNumber $text"
        binding.playlistlittleSongNumber.text = number

        val radius = itemView.resources.getDimensionPixelSize(R.dimen.trackCornerRadius)

        if (item.uri.isEmpty()) {
            val imageResource = R.drawable.musicalbum
            binding.playlistlittleCover.setImageResource(imageResource)
            val layoutParams = binding.playlistlittleCover.layoutParams
            layoutParams.width = 104
            layoutParams.height = 103
            binding.playlistlittleCover.layoutParams = layoutParams
        } else {

            val width = 160
            val height = 160
            Glide.with(itemView)
                .load(item.uri)
                .placeholder(R.drawable.musicalbum)
                .transform(CenterCrop(), RoundedCorners(radius))
                .override(width, height)
                .into(binding.playlistlittleCover)
        }
    }
}