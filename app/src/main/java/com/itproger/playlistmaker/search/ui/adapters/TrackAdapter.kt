package com.itproger.playlistmaker.search.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itproger.playlistmaker.search.domain.models.Track
import com.itproger.playlistmaker.databinding.TrackViewBinding

class TrackAdapter(

    private val onClickListener: (clickedTrack: Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {

    var trackList: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = TrackViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding)
    }

    override fun getItemCount(): Int = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val currentTrack = trackList[position]
        holder.bind(currentTrack)
        holder.itemView.setOnClickListener {
            onClickListener(currentTrack)

        }
    }

    fun setData(newTracks: List<Track>) {
        trackList.clear()
        trackList.addAll(newTracks)
    }
}