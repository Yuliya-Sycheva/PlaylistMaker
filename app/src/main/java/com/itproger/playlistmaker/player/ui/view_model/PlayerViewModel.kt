package com.itproger.playlistmaker.player.ui.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.itproger.playlistmaker.player.domain.interactor.PlayerInteractor
import com.itproger.playlistmaker.player.ui.models.PlayerStateInterface
import com.itproger.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerViewModel(
    private val playerInteractor : PlayerInteractor
) : ViewModel() {

    companion object {
        const val DELAY = 500L
        const val TRACK_FINISH = 29_900L
        const val MISTAKE = "Mistake"
    }

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<PlayerStateInterface>()
    fun observeState(): LiveData<PlayerStateInterface> = stateLiveData

    private fun startTimer() {
        mainThreadHandler?.postDelayed(
            object : Runnable {
                override fun run() {
                    Log.d(MISTAKE, "Timer")
                    val maxTrackDuration: Long =
                        if (playerInteractor.playerDuration > TRACK_FINISH) {
                            TRACK_FINISH
                        } else {
                            (playerInteractor.playerDuration.toLong())
                        }

                    if (playerInteractor.playerCurrentPosition < maxTrackDuration) {
                        renderState(
                            PlayerStateInterface.UpdatePlayingTime(
                                SimpleDateFormat(
                                    "mm:ss",
                                    Locale.getDefault()
                                ).format(playerInteractor.playerCurrentPosition)
                            )
                        )
                    } else {
                        renderState((PlayerStateInterface.Prepare))
                    }
                    // И снова планируем то же действие через пол секунды
                    mainThreadHandler?.postDelayed(
                        this,
                        DELAY,
                    )

                }
            },
            DELAY
        )
    }

    private fun renderState(state: PlayerStateInterface) {
        stateLiveData.postValue(state)
    }

    fun preparePlayer(track: Track) {
        playerInteractor.preparePlayer(
            track,
            onPreparedListener = {
                renderState(PlayerStateInterface.Prepare)
                Log.d(MISTAKE, "onPreparedListener")
            },
            onPlayerCompletion = {
                renderState(PlayerStateInterface.Prepare)
                mainThreadHandler?.removeCallbacksAndMessages(null)
                Log.d(MISTAKE, "onPlayerCompletion")
            }
        )
    }

    fun startPlayer() {
        renderState(PlayerStateInterface.Play)
        playerInteractor.startPlayer()
        startTimer()
        Log.d(MISTAKE, "Start")
    }

    fun pausePlayer() {
        renderState(PlayerStateInterface.Pause)
        playerInteractor.pausePlayer()
        mainThreadHandler?.removeCallbacksAndMessages(null)
        Log.d(MISTAKE, "Pause")
    }

    fun releasePlayer() {
        playerInteractor.releasePlayer()
        Log.d(MISTAKE, "Destroy")
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