package com.itproger.playlistmaker.player.domain.interactor.impl

import com.itproger.playlistmaker.player.domain.repository.PlayerRepository
import com.itproger.playlistmaker.player.domain.interactor.PlayerInteractor
import com.itproger.playlistmaker.search.domain.models.Track


class PlayerInteractorImpl(
    private var playerRepository: PlayerRepository
) : PlayerInteractor {
    override var onPlayerStateChanged: (state: Int) -> Unit
        get() = playerRepository.onPlayerStateChanged
        set(value) {
            playerRepository.onPlayerStateChanged = value
        }
    override var onPlayerCompletion: () -> Unit
        get() = playerRepository.onPlayerCompletion
        set(value) {
            playerRepository.onPlayerCompletion = value
        }
    override val playerDuration: Int
        get() = playerRepository.playerDuration
    override val playerCurrentPosition: Int
        get() = playerRepository.playerCurrentPosition

    override fun preparePlayer(track: Track) {
        playerRepository.preparePlayer(track)
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun playbackControl() {
        playerRepository.playbackControl()
    }

    override fun releasePlayer() {
        playerRepository.releasePlayer()
    }
}