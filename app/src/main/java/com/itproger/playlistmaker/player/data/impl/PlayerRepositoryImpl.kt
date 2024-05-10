package com.itproger.playlistmaker.player.data.impl

import android.media.MediaPlayer
import android.util.Log
import com.itproger.playlistmaker.player.domain.models.PlayerScreenState
import com.itproger.playlistmaker.player.domain.repository.PlayerRepository
import com.itproger.playlistmaker.player.ui.view_model.PlayerViewModel
import com.itproger.playlistmaker.search.domain.models.Track


class PlayerRepositoryImpl(
   // private val mediaPlayer: MediaPlayer

) : PlayerRepository {

    private val mediaPlayer= MediaPlayer()  //add here

        //  private var localOnPlayerCompletion: () -> Unit = {}

//    init {
//        mediaPlayer.setOnPreparedListener {
//            Log.d(PlayerViewModel.MISTAKE, "setOnPreparedListener")
//        }
//        mediaPlayer.setOnCompletionListener {
//            localOnPlayerCompletion.invoke()
//            Log.d(PlayerViewModel.MISTAKE, "localOnPlayerCompletion.invoke()")
//        }
//    }

//    override var onPlayerCompletion: () -> Unit
//        get() = localOnPlayerCompletion
//        set(value) {
//            localOnPlayerCompletion = value
//        }

    override val playerDuration: Int
        get() = mediaPlayer.duration
    override val playerCurrentPosition: Int
        get() = mediaPlayer.currentPosition

    override fun preparePlayer(track: Track, onPreparedListener: () -> Unit, onPlayerCompletion: () -> Unit) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPreparedListener.invoke()
            Log.d(PlayerViewModel.MISTAKE, "onPreparedListener.invoke()")
        }
        mediaPlayer.setOnCompletionListener {
            onPlayerCompletion.invoke()
            Log.d(PlayerViewModel.MISTAKE, "localOnPlayerCompletion.invoke()")
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

//    override fun playbackControl() {  //убрать
//        when (playerState) {
//            PlayerScreenState.PLAYING -> {
//                pausePlayer()
//            }
//
//            PlayerScreenState.PREPARED, PlayerScreenState.PAUSED -> {
//                startPlayer()
//            }
//
//            else -> {}
//        }
//    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }
}