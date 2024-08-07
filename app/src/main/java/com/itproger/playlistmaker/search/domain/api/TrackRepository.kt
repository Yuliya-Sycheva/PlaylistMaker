package com.itproger.playlistmaker.search.domain.api

import com.itproger.playlistmaker.search.domain.models.Track
import com.itproger.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTracks(text: String): Flow<Resource<List<Track>>>

    fun saveTrackToHistory(track: List<Track>)

    fun readTracksFromHistory(): List<Track>

    fun clearHistory()

}