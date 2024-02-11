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
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val trackJson = intent.getStringExtra("clicked_track")
        val track = Gson().fromJson(trackJson, Track::class.java)

        val trackName = findViewById<TextView>(R.id.trackName)
        val artistName = findViewById<TextView>(R.id.artistName)
        val trackTimeMillis: TextView = findViewById(R.id.track_time)
        val duration: TextView = findViewById(R.id.play_time)
        val artworkUrlView: ImageView = findViewById(R.id.cover)
        val collectionName = findViewById<TextView>(R.id.track_album)
        val releaseDate = findViewById<TextView>(R.id.track_year)
        val primaryGenreName = findViewById<TextView>(R.id.track_genre)
        val country = findViewById<TextView>(R.id.track_country)
        val album: TextView = findViewById<TextView>(R.id.album)

        val cornerRadius = 8f
        val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
        val releaseYear = track.releaseDate.substring(0, 4)

        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTimeMillis.text = dateFormat.format(track.trackTimeMillis)
        duration.text = dateFormat.format(track.trackTimeMillis)

        if (track.collectionName.isNullOrEmpty()) {
            collectionName.visibility=View.GONE
            album.visibility=View.GONE
        } else {
            collectionName.text = track.collectionName
        }
        releaseDate.text = releaseYear
        primaryGenreName.text = track.primaryGenreName
        country.text = track.country

        Glide.with(applicationContext)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .centerCrop()
            .transform(RoundedCorners(dpToPx(cornerRadius, applicationContext)))
            .placeholder(R.drawable.placeholder)
            .into(artworkUrlView)

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
