package com.itproger.playlistmaker.search.domain.api

import com.itproger.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTracks(text: String) : Flow<Pair<List<Track>?, String?>>

    fun saveTrackToHistory(track: List<Track>)

    fun readTracksFromHistory(): List<Track>

    fun clearHistory()
}