package com.itproger.playlistmaker.player.domain.repository

import com.itproger.playlistmaker.player.domain.models.PlayerScreenState
import com.itproger.playlistmaker.search.domain.models.Track


interface PlayerRepository {

  //  var onPlayerStateChanged: (state: PlayerScreenState) -> Unit //убрать
    var onPlayerCompletion: () -> Unit
    val playerDuration: Int
    val playerCurrentPosition: Int

    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
  //  fun playbackControl()
    fun isPlaying(): Boolean
    fun releasePlayer()
}