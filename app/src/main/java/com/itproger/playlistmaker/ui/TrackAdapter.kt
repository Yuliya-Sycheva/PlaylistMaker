package com.itproger.playlistmaker.ui
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itproger.playlistmaker.R
import com.itproger.playlistmaker.domain.models.Track

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

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }
}