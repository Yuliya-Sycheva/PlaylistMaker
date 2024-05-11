package com.itproger.playlistmaker.search.ui.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.itproger.playlistmaker.utils.GeneralFunctions
import com.itproger.playlistmaker.R
import com.itproger.playlistmaker.databinding.TrackViewBinding
import com.itproger.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(private val binding: TrackViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val cornerRadius = 2f
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    fun bind(model: Track) {
        with(binding) {
            trackNameTv.text = model.trackName
            artistName.text = model.artistName
            trackTime.text = dateFormat.format(model.trackTimeMillis)
        }
        Glide.with(itemView).load(model.artworkUrl100).centerCrop()
            .transform(RoundedCorners(GeneralFunctions.dpToPx(cornerRadius, itemView.context)))
            .placeholder(R.drawable.placeholder).into(binding.trackIcon)
    }
}
