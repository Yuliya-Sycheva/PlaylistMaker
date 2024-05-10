package com.itproger.playlistmaker.player.ui.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.itproger.playlistmaker.R
import com.itproger.playlistmaker.player.creator.PlayerCreator
import com.itproger.playlistmaker.player.domain.models.PlayerScreenState
import com.itproger.playlistmaker.player.ui.PlayerActivity
import com.itproger.playlistmaker.player.ui.models.PlayerStateInterface
import com.itproger.playlistmaker.search.domain.models.Track
import com.itproger.playlistmaker.search.ui.models.SearchScreenState
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerViewModel(
    application: Application,
) : AndroidViewModel(application) {


    companion object {
        const val DELAY = 500L
        const val TRACK_FINISH = 29_900L
        const val MISTAKE = "Mistake"

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }

    private val playerInteractor =
        PlayerCreator.providePlayerInteractor()

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<PlayerStateInterface>()
    fun observeState(): LiveData<PlayerStateInterface> = stateLiveData

//    init { // тут могут быть ошибки по тому, как разнесла
//        playerInteractor.onPlayerStateChanged = { state ->
//            when (state) {
//                PlayerScreenState.PLAYING -> {
//                    startTimer()   // тут особенно
//                    renderState(PlayerStateInterface.Play)
//                  //  binding.playButton.setImageResource(R.drawable.pause)
//                }
//
//                PlayerScreenState.PREPARED, PlayerScreenState.PAUSED, PlayerScreenState.COMPLETED -> {
//                    renderState(PlayerStateInterface.Prepare)
//                 //   binding.playButton.setImageResource(R.drawable.play)
//                }
//
//                else -> {}
//            }
//        }
//    }

    init {
        playerInteractor.onPlayerCompletion = {
            renderState(PlayerStateInterface.Prepare)
            Log.d(MISTAKE, "Completed")
            mainThreadHandler?.removeCallbacksAndMessages(null)  //gpt
            releasePlayer() //gpt
        }
    }


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
                    renderState(
                        PlayerStateInterface.UpdatePlayingTime(
                            time =
                            //                     if (mediaPlayer.currentPosition < maxTrackDuration) {
                            if (playerInteractor.playerCurrentPosition < maxTrackDuration) {
                                SimpleDateFormat(
                                    "mm:ss",
                                    Locale.getDefault()
                                ).format(playerInteractor.playerCurrentPosition)
                            } else {
                                String.format("%02d:%02d", 0, 0)
                            }
                        )
                    )
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
        renderState(PlayerStateInterface.Prepare)
        playerInteractor.preparePlayer(track)
        Log.d(MISTAKE, "Prepare")
    }

    fun startPlayer() {
        renderState(PlayerStateInterface.Play)
        playerInteractor.startPlayer()
        startTimer() //??????????????????????????????????????????????????????
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
}