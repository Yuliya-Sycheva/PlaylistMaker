package com.itproger.playlistmaker.player.domain.interactor.impl

import android.util.Log
import com.itproger.playlistmaker.player.domain.repository.PlayerRepository
import com.itproger.playlistmaker.player.domain.interactor.PlayerInteractor
import com.itproger.playlistmaker.player.domain.models.PlayerScreenState
import com.itproger.playlistmaker.search.domain.models.Track


class PlayerInteractorImpl(
    private var playerRepository: PlayerRepository
) : PlayerInteractor {
//    override var onPlayerStateChanged: (state: PlayerScreenState) -> Unit
//        get() = playerRepository.onPlayerStateChanged
//        set(value) {
//            playerRepository.onPlayerStateChanged = value
//        }
//    override var onPlayerCompletion: () -> Unit
//        get() = playerRepository.onPlayerCompletion
//        set(value) {
//            Log.d("Mistake", "Completion callback called")
//            playerRepository.onPlayerCompletion = value
//        }
    override val playerDuration: Int
        get() = playerRepository.playerDuration
    override val playerCurrentPosition: Int
        get() = playerRepository.playerCurrentPosition

    override fun preparePlayer(track: Track, onPreparedListener: () -> Unit, onPlayerCompletion: () -> Unit) {
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

//    override fun playbackControl() {
//        playerRepository.playbackControl()
//    }

    override fun releasePlayer() {
        playerRepository.releasePlayer()
    }
}