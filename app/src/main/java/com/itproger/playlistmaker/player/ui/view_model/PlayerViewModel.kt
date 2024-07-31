package com.itproger.playlistmaker.player.ui.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.itproger.playlistmaker.player.domain.interactor.PlayerInteractor
import com.itproger.playlistmaker.player.ui.models.PlayerStateInterface
import com.itproger.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerViewModel(
    private val playerInteractor : PlayerInteractor
) : ViewModel() {

    private companion object {
        const val DELAY = 300L
    }

    private var timerJob: Job? = null

    private val stateLiveData = MutableLiveData<PlayerStateInterface>()
    fun observeState(): LiveData<PlayerStateInterface> = stateLiveData

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(DELAY)
                stateLiveData.postValue(PlayerStateInterface.UpdatePlayingTime(getCurrentPlayerPosition()))
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerInteractor.playerCurrentPosition) ?: "00:00"
    }

    private fun renderState(state: PlayerStateInterface) {
        stateLiveData.postValue(state)
    }

    fun preparePlayer(track: Track) {
        playerInteractor.preparePlayer(
            track,
            onPreparedListener = {
                renderState(PlayerStateInterface.Prepare)
            },
            onPlayerCompletion = {
                renderState(PlayerStateInterface.Prepare)
                timerJob?.cancel()
            }
        )
    }

    fun startPlayer() {
        renderState(PlayerStateInterface.Play)
        playerInteractor.startPlayer()
        startTimer()
    }

    fun pausePlayer() {
        renderState(PlayerStateInterface.Pause)
        playerInteractor.pausePlayer()
        timerJob?.cancel()
    }

    fun releasePlayer() {
        playerInteractor.releasePlayer()
    }

    fun isPlaying(): Boolean {
        return playerInteractor.isPlaying()
    }

    fun playbackControl() {
        if (isPlaying()) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    fun setStartTime() : String {
        return String.format("%02d:%02d", 0, 0)
    }
}