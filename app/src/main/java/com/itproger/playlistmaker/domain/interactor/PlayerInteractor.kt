package com.itproger.playlistmaker.domain.interactor

import android.media.MediaPlayer
import com.itproger.playlistmaker.domain.models.Track

interface PlayerInteractor {
    var onPlayerStateChanged: (state: Int) -> Unit
    var onPlayerCompletion: () -> Unit
    val playerDuration: Int
    val playerCurrentPosition: Int
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun releasePlayer()
}