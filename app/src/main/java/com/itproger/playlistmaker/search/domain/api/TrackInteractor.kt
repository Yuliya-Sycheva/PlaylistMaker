package com.itproger.playlistmaker.search.domain.api

import com.itproger.playlistmaker.search.domain.models.Track

interface TrackInteractor {
    fun searchTracks(text: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }

    fun saveTrackToHistory(track: List<Track>)

    fun readTracksFromHistory(): Array<Track>   //не забыть заменить на List

    fun clearHistory()
}