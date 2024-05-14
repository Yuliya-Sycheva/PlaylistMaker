package com.itproger.playlistmaker.player.data.impl

import android.media.MediaPlayer
import android.util.Log
import com.itproger.playlistmaker.player.domain.repository.PlayerRepository
import com.itproger.playlistmaker.search.domain.models.Track


class PlayerRepositoryImpl : PlayerRepository {

    companion object {
        const val MISTAKE = "Mistake"
    }

    private val mediaPlayer = MediaPlayer()

    override val playerDuration: Int
        get() = mediaPlayer.duration
    override val playerCurrentPosition: Int
        get() = mediaPlayer.currentPosition

    override fun preparePlayer(
        track: Track,
        onPreparedListener: () -> Unit,
        onPlayerCompletion: () -> Unit
    ) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPreparedListener.invoke()
            Log.d(MISTAKE, "onPreparedListener.invoke()")
        }
        mediaPlayer.setOnCompletionListener {
            onPlayerCompletion.invoke()
            Log.d(MISTAKE, "localOnPlayerCompletion.invoke()")
        }

    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }
}