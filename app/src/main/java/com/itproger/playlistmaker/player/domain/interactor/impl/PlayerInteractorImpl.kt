package com.itproger.playlistmaker.player.domain.interactor.impl

import com.itproger.playlistmaker.player.domain.repository.PlayerRepository
import com.itproger.playlistmaker.player.domain.interactor.PlayerInteractor
import com.itproger.playlistmaker.search.domain.models.Track


class PlayerInteractorImpl(
    private var playerRepository: PlayerRepository
) : PlayerInteractor {

    override val playerCurrentPosition: Int
        get() = playerRepository.playerCurrentPosition

    override fun preparePlayer(
        track: Track,
        onPreparedListener: () -> Unit,
        onPlayerCompletion: () -> Unit
    ) {
        playerRepository.preparePlayer(track, onPreparedListener, onPlayerCompletion)
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun isPlaying(): Boolean {
        return playerRepository.isPlaying()
    }

    override fun releasePlayer() {
        playerRepository.releasePlayer()
    }
}