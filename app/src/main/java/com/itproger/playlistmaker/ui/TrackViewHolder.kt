package com.itproger.playlistmaker.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.itproger.playlistmaker.GeneralFunctions
import com.itproger.playlistmaker.R
import com.itproger.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackNameView: TextView = itemView.findViewById(R.id.trackNameTv)
    private val artistNameView: TextView = itemView.findViewById(R.id.artistName)
    private val trackTimeView: TextView = itemView.findViewById(R.id.trackTime)
    private val artworkUrlView: ImageView = itemView.findViewById(R.id.trackIcon)

    private val cornerRadius = 2f
    private val dateFormat by lazy{SimpleDateFormat("mm:ss", Locale.getDefault())}

    fun bind(model: Track) {
        trackNameView.text = model.trackName
        artistNameView.text = model.artistName
        trackTimeView.text = dateFormat.format(model.trackTimeMillis)
        Glide.with(itemView).load(model.artworkUrl100).centerCrop()
            .transform(RoundedCorners(GeneralFunctions.dpToPx(cornerRadius, itemView.context)))
            .placeholder(R.drawable.placeholder).into(artworkUrlView)
    }
}
