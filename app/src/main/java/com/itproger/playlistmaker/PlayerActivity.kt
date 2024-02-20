package com.itproger.playlistmaker

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.itproger.playlistmaker.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackJson = intent.getStringExtra("clicked_track")
        val track = Gson().fromJson(trackJson, Track::class.java)

        val cornerRadius = 8f
        val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
        val releaseYear = track.releaseDate.substring(0, 4)

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackDuration.text = dateFormat.format(track.trackTimeMillis)
        binding.playTime.text = dateFormat.format(track.trackTimeMillis)

        if (track.collectionName.isNullOrEmpty()) {
            binding.trackAlbum.visibility=View.GONE
            binding.album.visibility=View.GONE
        } else {
            binding.trackAlbum.text = track.collectionName
        }
        binding.releaseDate.text = releaseYear
        binding.trackGenre.text = track.primaryGenreName
        binding.trackCountry.text = track.country

        Glide.with(applicationContext)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .centerCrop()
            .transform(RoundedCorners(dpToPx(cornerRadius, applicationContext)))
            .placeholder(R.drawable.placeholder)
            .into(binding.cover)

        val imageBack = findViewById<ImageView>(R.id.back)
        imageBack.setOnClickListener {
            finish()
        }
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
        ).toInt()
    }
}
