package com.itproger.playlistmaker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itproger.playlistmaker.search.domain.api.TrackInteractor
import com.itproger.playlistmaker.search.domain.models.Track
import com.itproger.playlistmaker.search.ui.models.SearchScreenState

class TracksSearchViewModel(
    private val trackInteractor: TrackInteractor
) : ViewModel() {

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<SearchScreenState>()
    fun observeState(): LiveData<SearchScreenState> = stateLiveData

    private var latestSearchText: String? = null

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchTrack(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun searchTrack(newSearchText: String) {
        if (newSearchText.isBlank()
        ) {  // чтобы после крестика не выскакивало, что ничего не нашлось
            return
        }

        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)  //чтобы не происходил повторный поиск при нажатии на галочку

        renderState(SearchScreenState.Loading)

        trackInteractor.searchTracks(newSearchText, object : TrackInteractor.TrackConsumer {
            override fun consume(
                foundTracks: List<Track>?,
                errorMessage: String?
            ) {
                val tracks = mutableListOf<Track>()

                if (foundTracks != null) {
                    tracks.clear()   //нет в примере
                    tracks.addAll(foundTracks)
                }
                when {
                    errorMessage != null -> {
                        Log.d("TEST", "errorMessage")
                        renderState(
                            SearchScreenState.Error(
                                errorMessage
                            )
                        )
                    }

                    foundTracks?.isEmpty() == true -> {
                        Log.d("TEST", "isEmpty")
                        renderState(
                            SearchScreenState.Empty
                        )
                    }

                    else -> {
                        renderState(SearchScreenState.Content(tracks = tracks))
                    }
                }
            }
        }
        )
    }

    fun onClickedTrack(track: List<Track>) {
        saveTrackToHistory(track)
    }

    private fun renderState(state: SearchScreenState) {
        stateLiveData.postValue(state)
    }

    private fun saveTrackToHistory(track: List<Track>) {
        trackInteractor.saveTrackToHistory(track)
    }

    fun readTracksFromHistory(): List<Track> {
        return trackInteractor.readTracksFromHistory()
    }

    fun clearHistory() {
        trackInteractor.clearHistory()
    }

    fun onClearIconClick() {
        val historyTracks = readTracksFromHistory()
        renderState(SearchScreenState.History(historyTracks.toMutableList()))
    }
}