package com.itproger.playlistmaker.player.ui

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
import com.itproger.playlistmaker.utils.GeneralFunctions
import com.itproger.playlistmaker.R
import com.itproger.playlistmaker.player.domain.repository.PlayerRepository
import com.itproger.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.itproger.playlistmaker.player.data.impl.PlayerRepositoryImpl.Companion.STATE_COMPLETED
import com.itproger.playlistmaker.player.data.impl.PlayerRepositoryImpl.Companion.STATE_PAUSED
import com.itproger.playlistmaker.player.data.impl.PlayerRepositoryImpl.Companion.STATE_PLAYING
import com.itproger.playlistmaker.player.data.impl.PlayerRepositoryImpl.Companion.STATE_PREPARED

import com.itproger.playlistmaker.databinding.ActivityPlayerBinding
import com.itproger.playlistmaker.player.domain.interactor.PlayerInteractor
import com.itproger.playlistmaker.player.domain.interactor.impl.PlayerInteractorImpl
import com.itproger.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private var mainThreadHandler: Handler? = null

    private var mediaPlayer = MediaPlayer()
    private val playerRepository: PlayerRepository = PlayerRepositoryImpl(mediaPlayer)
    private val playerInteractor: PlayerInteractor = PlayerInteractorImpl(playerRepository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerInteractor.onPlayerStateChanged = { state ->
            when (state) {
                STATE_PLAYING -> {
                    startTimer()
                    binding.playButton.setImageResource(R.drawable.pause)
                }

                STATE_PREPARED, STATE_PAUSED, STATE_COMPLETED -> {
                    binding.playButton.setImageResource(R.drawable.play)
                }
            }
        }

        playerInteractor.onPlayerCompletion = {
            binding.playButton.setImageResource(R.drawable.play)
            binding.playTime.text = String.format("%02d:%02d", 0, 0)
            Log.d(MISTAKE, "OnCompletionListener")
        }

        mainThreadHandler = Handler(Looper.getMainLooper())


        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(CLICKED_TRACK, Track::class.java)
        } else {
            intent.getParcelableExtra(CLICKED_TRACK)
        }

        if (track != null) {
            setTrackData(track)
            playerInteractor.preparePlayer(track)
        }

        binding.playButton.setOnClickListener {
            playerInteractor.playbackControl()
        }
    }

    override fun onPause() {
        super.onPause()
        playerInteractor.pausePlayer()
        mainThreadHandler?.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.releasePlayer()
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

    private fun startTimer() {
        mainThreadHandler?.postDelayed(
            object : Runnable {
                override fun run() {
                    val maxTrackDuration: Long =
                        if (playerInteractor.playerDuration > TRACK_FINISH) {
                            TRACK_FINISH
                        } else {
                            (playerInteractor.playerDuration.toLong())
                        }
                    binding.playTime.text =
                        if (mediaPlayer.currentPosition < maxTrackDuration) {
                            SimpleDateFormat(
                                "mm:ss",
                                Locale.getDefault()
                            ).format(playerInteractor.playerCurrentPosition)
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

    private companion object {
        const val CLICKED_TRACK: String = "clicked_track"
        const val DELAY = 500L
        const val TRACK_FINISH = 29_900L
        const val MISTAKE = "Mistake"
    }
}
