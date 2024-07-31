package com.itproger.playlistmaker.search.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itproger.playlistmaker.search.domain.api.TrackInteractor
import com.itproger.playlistmaker.search.domain.models.Track
import com.itproger.playlistmaker.search.ui.models.SearchScreenState
import com.itproger.playlistmaker.utils.debounce
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TracksSearchViewModel(
    private val trackInteractor: TrackInteractor
) : ViewModel() {

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private var searchDebounceJob: Job? = null

    private val stateLiveData = MutableLiveData<SearchScreenState>()
    fun observeState(): LiveData<SearchScreenState> = stateLiveData

    private var latestSearchText: String? = null

    override fun onCleared() {
        searchDebounceJob?.cancel()
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        this.latestSearchText = changedText

        searchDebounceJob?.cancel()
        searchDebounceJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchTrack(changedText)
        }
    }

    fun searchTrack(newSearchText: String) {
        if (newSearchText.isBlank()
        ) {  // чтобы после крестика не выскакивало, что ничего не нашлось
            return
        }

        searchDebounceJob?.cancel()  //чтобы не происходил повторный поиск при нажатии на галочку, потому не стала исп-ть debounce()

        renderState(SearchScreenState.Loading)

        viewModelScope.launch {
            trackInteractor.searchTracks(newSearchText)
                .collect { pair ->
                    processResult(pair.first, pair.second)
                }
        }
    }

    private fun processResult(
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