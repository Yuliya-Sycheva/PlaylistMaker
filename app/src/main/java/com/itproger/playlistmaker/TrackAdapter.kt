package com.itproger.playlistmaker
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    private val trackList: MutableList<Track>,
    private val searchHistory: SearchHistory
) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val currentTrack = trackList[position]
        holder.bind(currentTrack)
        holder.itemView.setOnClickListener{
           // Toast.makeText(holder.itemView.context, "Нажали на трек!", Toast.LENGTH_SHORT).show()
            searchHistory.saveTrack(mutableListOf(currentTrack))

        }
    }
    fun setData(newTracks: MutableList<Track>) {
        trackList.clear()
        trackList.addAll(newTracks)
        notifyDataSetChanged()
    }
}