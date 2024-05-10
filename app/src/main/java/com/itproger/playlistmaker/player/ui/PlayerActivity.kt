package com.itproger.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.itproger.playlistmaker.utils.GeneralFunctions
import com.itproger.playlistmaker.R
import com.itproger.playlistmaker.databinding.ActivityPlayerBinding
import com.itproger.playlistmaker.player.ui.models.PlayerStateInterface
import com.itproger.playlistmaker.player.ui.view_model.PlayerViewModel
import com.itproger.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory()
        )[PlayerViewModel::class.java]
        viewModel.observeState().observe(this) {
            render(it)
        }

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(CLICKED_TRACK, Track::class.java)
        } else {
            intent.getParcelableExtra(CLICKED_TRACK)
        }

        if (track != null) {
            setTrackData(track)
            viewModel.preparePlayer(track)
        }

        binding.playButton.setOnClickListener {
            viewModel.playbackControl()
        }
    } // конец onCreate()

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
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
        binding.playButton.setBackgroundColor(  //добавила для прозрачного фона за кнопками
            ContextCompat.getColor(
                this,
                android.R.color.transparent
            )
        )

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

        binding.back.setOnClickListener {
            finish()
        }
    }

    private fun render(state: PlayerStateInterface) {
        when (state) {
            is PlayerStateInterface.Prepare -> prepare()
            is PlayerStateInterface.Play -> play()
            is PlayerStateInterface.Pause -> pause()
            is PlayerStateInterface.UpdatePlayingTime -> updatePlayingTime(state.time)
            else -> {}
        }
    }



    private fun prepare() {  //??????
        binding.playButton.setImageResource(R.drawable.play)
        binding.playTime.text = String.format("%02d:%02d", 0, 0)
    }

    private fun play() {
        binding.playButton.setImageResource(R.drawable.pause)
    }

    private fun pause() {
        binding.playButton.setImageResource(R.drawable.play)
    }

    private fun updatePlayingTime(time: String) {
        binding.playTime.text = time
    }

    private companion object {
        const val CLICKED_TRACK: String = "clicked_track"
    }
}
