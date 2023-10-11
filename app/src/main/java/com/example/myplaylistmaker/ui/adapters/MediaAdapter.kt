package com.example.myplaylistmaker.ui.adapters

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.domain.search.models.Track

class MediaAdapter(
    private val onItemClicked: (Track) -> Unit
) : RecyclerView.Adapter<MediaViewHolder>() {

    companion object {
        private const val CLICK_THROTTLE_DELAY = 1000L
    }

    private val listDiffer = AsyncListDiffer(this, TrackItemCallback())
    private var isThrottling = false
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.track_item, parent, false)
            .let { view ->
                MediaViewHolder(view) { track ->
                    if (!isThrottling) {
                        isThrottling = true
                        handler.postDelayed(
                            {
                                onItemClicked.invoke(track)
                                isThrottling = false
                            },
                            CLICK_THROTTLE_DELAY
                        )
                    }
                }
            }
    }


    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(listDiffer.currentList[position])
    }

    override fun getItemCount(): Int = listDiffer.currentList.size

    fun setItems(newItems: List<Track>) {
        listDiffer.submitList(newItems)
    }

    private class TrackItemCallback : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.trackId == newItem.trackId
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem == newItem
        }
    }
}

