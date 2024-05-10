package com.itproger.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.itproger.playlistmaker.player.domain.models.PlayerScreenState
import com.itproger.playlistmaker.player.domain.repository.PlayerRepository
import com.itproger.playlistmaker.search.domain.models.Track


class PlayerRepositoryImpl(
   // private val mediaPlayer: MediaPlayer

) : PlayerRepository {

    private val mediaPlayer= MediaPlayer()  //add here

  //  private var playerState = PlayerScreenState.DEFAULT

  //  private var localOnPlayerStateChanged: (state: PlayerScreenState) -> Unit = {}
    private var localOnPlayerCompletion: () -> Unit = {}

    init {
        mediaPlayer.setOnPreparedListener {
     //       playerState = PlayerScreenState.PREPARED
      //      onPlayerStateChanged(playerState)
        }
        mediaPlayer.setOnCompletionListener {
   //         playerState = PlayerScreenState.COMPLETED
   //         onPlayerStateChanged(playerState)
            localOnPlayerCompletion.invoke()
        }
    }

//    override var onPlayerStateChanged: (state: PlayerScreenState) -> Unit
//        get() = localOnPlayerStateChanged
//        set(value) {
//            localOnPlayerStateChanged = value
//        }

    override var onPlayerCompletion: () -> Unit
        get() = localOnPlayerCompletion
        set(value) {
            localOnPlayerCompletion = value
        }

    override val playerDuration: Int
        get() = mediaPlayer.duration
    override val playerCurrentPosition: Int
        get() = mediaPlayer.currentPosition


    override fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
    }

    override fun startPlayer() {
        mediaPlayer.start()
 //       playerState = PlayerScreenState.PLAYING
//        onPlayerStateChanged(playerState)
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    //    playerState = PlayerScreenState.PAUSED
    //    onPlayerStateChanged(playerState)

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

//    companion object {
//        const val STATE_DEFAULT = 0
//        const val STATE_PREPARED = 1
//        const val STATE_PLAYING = 2
//        const val STATE_PAUSED = 3
//        const val STATE_COMPLETED = 4
//    }
}