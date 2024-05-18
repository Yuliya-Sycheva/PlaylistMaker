package com.itproger.playlistmaker.search.ui.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.itproger.playlistmaker.R
import com.itproger.playlistmaker.search.creator.SearchCreator
import com.itproger.playlistmaker.search.domain.api.TrackInteractor
import com.itproger.playlistmaker.search.domain.models.Track
import com.itproger.playlistmaker.search.ui.models.SearchScreenState

class TracksSearchViewModel(
    application: Application,
) : AndroidViewModel(application) {

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TracksSearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    private val trackInteractor =
        SearchCreator.provideTrackInteractor(getApplication<Application>())

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
                                errorMessage = getApplication<Application>().getString(R.string.something_went_wrong)
                            )
                        )
                    }

                    foundTracks?.isEmpty() == true -> {
                        Log.d("TEST", "isEmpty")
                        renderState(
                            SearchScreenState.Empty(
                                message = getApplication<Application>().getString(
                                    R.string.nothing_found
                                )
                            )
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
        renderState(SearchScreenState.History(historyTracks as MutableList<Track>))
    }
}