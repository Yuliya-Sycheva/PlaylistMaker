package com.itproger.playlistmaker.search.domain.impl

import com.itproger.playlistmaker.search.domain.api.TrackInteractor
import com.itproger.playlistmaker.search.domain.api.TrackRepository
import com.itproger.playlistmaker.search.domain.models.Track
import com.itproger.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    override fun searchTracks(text: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(text).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun saveTrackToHistory(track: List<Track>) {
        repository.saveTrackToHistory(track)
    }

    override fun readTracksFromHistory(): List<Track> {
        return repository.readTracksFromHistory()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}