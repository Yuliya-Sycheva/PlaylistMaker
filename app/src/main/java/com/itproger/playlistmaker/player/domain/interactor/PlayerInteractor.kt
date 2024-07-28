package com.itproger.playlistmaker.player.domain.interactor

import com.itproger.playlistmaker.search.domain.models.Track


interface PlayerInteractor {

    val playerCurrentPosition: Int

    fun preparePlayer(track: Track, onPreparedListener: () -> Unit, onPlayerCompletion: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun isPlaying(): Boolean
    fun releasePlayer()
}