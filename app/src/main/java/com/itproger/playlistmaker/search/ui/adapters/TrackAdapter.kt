package com.itproger.playlistmaker.search.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itproger.playlistmaker.R
import com.itproger.playlistmaker.search.domain.models.Track

class TrackAdapter(

    private val onClickListener: (clickedTrack: Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {

    var trackList: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val currentTrack = trackList[position]
        holder.bind(currentTrack)
        holder.itemView.setOnClickListener {
            // Toast.makeText(holder.itemView.context, "Нажали на трек!", Toast.LENGTH_SHORT).show()
            onClickListener(currentTrack)

        }
    }

    fun setData(newTracks: List<Track>) {
        trackList.clear()
        trackList.addAll(newTracks)
    }
}