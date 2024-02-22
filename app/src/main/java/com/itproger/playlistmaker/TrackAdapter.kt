package com.itproger.playlistmaker
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    private val trackList: MutableList<Track>,
    private val onClickListener: (clickedTrack : Track) -> Unit
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
            onClickListener(currentTrack)

        }
    }
    fun setData(newTracks: List<Track>) {
        trackList.clear()
        trackList.addAll(newTracks)
    }
}