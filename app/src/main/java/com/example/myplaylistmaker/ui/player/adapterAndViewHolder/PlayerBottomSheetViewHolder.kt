package com.example.myplaylistmaker.ui.player.adapterAndViewHolder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.BottomSheetPlaylistItemBinding
import com.example.myplaylistmaker.domain.playlist.Playlist

class PlayerBottomSheetViewHolder(
    private val binding: BottomSheetPlaylistItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Playlist) {
        binding.playlistName.text = item.playlistName
        val innerNumber = item.arrayNumber.toString()
        val text = when {
            innerNumber.toInt() % 10 == 1 && innerNumber.toInt() % 100 != 11 -> " трек"
            innerNumber.toInt() % 10 == 2 && innerNumber.toInt() % 100 != 12 -> " трека"
            innerNumber.toInt() % 10 == 3 && innerNumber.toInt() % 100 != 13 -> " трека"
            innerNumber.toInt() % 10 == 4 && innerNumber.toInt() % 100 != 14 -> " трека"
            else -> " треков"
        }
        val number = "$innerNumber $text"
        binding.tracksQuantity.text = number

        val radius = itemView.resources.getDimensionPixelSize(R.dimen.cornerRadius2)
        val width = 45
        val height = 45
        Glide.with(itemView)
            .load(item.uri)
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop(), RoundedCorners(radius))
            .override(width, height)
            .into(binding.trackImage)
    }
}

