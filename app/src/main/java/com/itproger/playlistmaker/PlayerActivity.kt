package com.itproger.playlistmaker

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.itproger.playlistmaker.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private var mainThreadHandler: Handler? = null

    private companion object {
        const val CLICKED_TRACK: String = "clicked_track"
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val DELAY = 500L
        const val TRACK_FINISH = 29_900L
        const val MISTAKE = "Mistake"
    }

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainThreadHandler = Handler(Looper.getMainLooper())


        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(CLICKED_TRACK, Track::class.java)
        } else {
            intent.getParcelableExtra(CLICKED_TRACK)
        }

        if (track != null) {
            setTrackData(track)
            preparePlayer(track)
        }

        binding.playButton.setOnClickListener {
            playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        Log.d(MISTAKE, "Destroy")
    }

    private fun setTrackData(track: Track) {
        val cornerRadius = 8f
        val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
        val releaseYear = track.releaseDate.substring(0, 4)

        with(binding) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackDuration.text = dateFormat.format(track.trackTimeMillis)
            playTime.text = String.format("%02d:%02d", 0, 0)
            playButton.setImageResource(R.drawable.play)
        }
        binding.playButton.setBackgroundColor(
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )
        //добавила для прозрачного фона за кнопками

        if (track.collectionName.isNullOrEmpty()) {
            binding.trackAlbum.visibility = View.GONE
            binding.album.visibility = View.GONE
        } else {
            binding.trackAlbum.text = track.collectionName
        }
        binding.releaseDate.text = releaseYear
        binding.trackGenre.text = track.primaryGenreName
        binding.trackCountry.text = track.country

        Glide.with(applicationContext)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .centerCrop()
            .transform(
                RoundedCorners(
                    GeneralFunctions.dpToPx(
                        cornerRadius,
                        applicationContext
                    )
                )
            )
            .placeholder(R.drawable.placeholder)
            .into(binding.cover)

        val imageBack = findViewById<ImageView>(R.id.back)
        imageBack.setOnClickListener {
            finish()
        }
    }

    private fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playButton.isEnabled = true
            playerState = STATE_PREPARED
            Log.d(MISTAKE, "preparePlayer")
        }
        mediaPlayer.setOnCompletionListener {
            binding.playButton.setImageResource(R.drawable.play)
            playerState = STATE_PREPARED
            binding.playTime.text = String.format("%02d:%02d", 0, 0)
            Log.d(MISTAKE, "OnCompletionListener")
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.playButton.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
        Log.d(MISTAKE, "startPlayer")
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playButton.setImageResource(R.drawable.play)
        playerState = STATE_PAUSED
        mainThreadHandler?.removeCallbacksAndMessages(null)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startTimer() {
        mainThreadHandler?.postDelayed(
            object : Runnable {
                override fun run() {
                    binding.playTime.text = if (mediaPlayer.currentPosition < TRACK_FINISH) {
                        SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(mediaPlayer.currentPosition)
                    } else {
                        String.format("%02d:%02d", 0, 0)
                    }
                    // И снова планируем то же действие через пол секунды
                    mainThreadHandler?.postDelayed(
                        this,
                        DELAY,
                    )

                }
            },
            DELAY
        )
    }
}
