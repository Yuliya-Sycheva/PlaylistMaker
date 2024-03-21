package com.itproger.playlistmaker.domain.interactor.impl

import com.itproger.playlistmaker.data.repository.PlayerRepository
import com.itproger.playlistmaker.domain.interactor.PlayerInteractor
import com.itproger.playlistmaker.domain.models.Track

class PlayerInteractorImpl(
    private val repository: PlayerRepository
) : PlayerInteractor {
    override fun preparePlayer(track: Track) {
        repository.preparePlayer(track)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun playbackControl() {
        repository.playbackControl()
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }
}