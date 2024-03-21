package com.itproger.playlistmaker.data.repository.impl

import android.media.MediaPlayer
import android.util.Log
import com.itproger.playlistmaker.R
import com.itproger.playlistmaker.data.repository.PlayerRepository
import com.itproger.playlistmaker.domain.models.Track
import com.itproger.playlistmaker.ui.player.PlayerActivity

class PlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
    private val onPlayerStateChanged: (state: Int) -> Unit,
    private val onPlayerCompletion: () -> Unit

) : PlayerRepository{

    private var playerState = STATE_DEFAULT

    init {
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            onPlayerStateChanged(playerState)
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_COMPLETED
            onPlayerStateChanged(playerState)
            //////////////////////////////////////////////////////////////добавить
        }
    }


    override fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
        onPlayerStateChanged(playerState)
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        onPlayerStateChanged(playerState)

    }

    override fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val STATE_COMPLETED = 4
        const val MISTAKE = "Mistake"
    }
}