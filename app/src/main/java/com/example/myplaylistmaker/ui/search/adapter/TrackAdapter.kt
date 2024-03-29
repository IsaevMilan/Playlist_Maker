package com.example.myplaylistmaker.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylistmaker.databinding.TrackItemBinding
import com.example.myplaylistmaker.domain.search.models.Track

class TrackAdapter(
    private val clickListener: TrackClick,
    private val longClickListener : LongClick
) : RecyclerView.Adapter<TrackViewHolder>() {

    private var _items: List<Track> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackItemBinding.inflate(layoutInspector, parent, false))
    }
    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(_items[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(_items[position])

        }
        holder.itemView.setOnLongClickListener {
        longClickListener.onLongClick(_items[position])
        return@setOnLongClickListener true
    }

    }
    override fun getItemCount(): Int {
        return _items.size
    }

    fun interface TrackClick {
        fun onClick(track: Track)

    }
    fun interface LongClick {
        fun onLongClick(track: Track)
    }

    fun setItems(items: List<Track>) {
        _items = items

    }
}