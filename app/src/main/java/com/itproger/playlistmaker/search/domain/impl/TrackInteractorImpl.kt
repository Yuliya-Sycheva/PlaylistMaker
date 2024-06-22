package com.itproger.playlistmaker.search.domain.impl

import com.itproger.playlistmaker.search.domain.api.TrackInteractor
import com.itproger.playlistmaker.search.domain.api.TrackRepository
import com.itproger.playlistmaker.search.domain.models.Track
import com.itproger.playlistmaker.utils.Resource
import java.util.concurrent.Executor

class TrackInteractorImpl(private val repository: TrackRepository, private val executor: Executor) : TrackInteractor {

    override fun searchTracks(text: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            when (val resource = repository.searchTracks(text)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
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