package com.itproger.playlistmaker.domain.interactor

import com.itproger.playlistmaker.domain.models.Track

interface PlayerInteractor {

    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun playbackControl()
    fun releasePlayer()
}