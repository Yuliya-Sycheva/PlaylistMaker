package com.itproger.playlistmaker.player.data.impl

import android.media.MediaPlayer
import android.util.Log
import com.itproger.playlistmaker.player.domain.repository.PlayerRepository
import com.itproger.playlistmaker.search.domain.models.Track


class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : PlayerRepository {

    companion object {
        const val MISTAKE = "Mistake"
    }

    override val playerCurrentPosition: Int
        get() = mediaPlayer.currentPosition

    override fun preparePlayer(
        track: Track,
        onPreparedListener: () -> Unit,
        onPlayerCompletion: () -> Unit
    ) {
        if (track.previewUrl != null) {
            mediaPlayer.setDataSource(track.previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                onPreparedListener.invoke()
            }
            mediaPlayer.setOnCompletionListener {
                onPlayerCompletion.invoke()
            }
        } else {
            Log.e(MISTAKE, "Preview URL is null")  //нужно это добавлять?
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