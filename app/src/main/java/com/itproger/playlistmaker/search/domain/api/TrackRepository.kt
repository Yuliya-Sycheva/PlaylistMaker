package com.itproger.playlistmaker.search.domain.api

import com.itproger.playlistmaker.search.domain.models.Track
import com.itproger.playlistmaker.utils.Resource

interface TrackRepository {
    fun searchTracks(text: String): Resource<List<Track>>

    fun saveTrackToHistory(track: List<Track>)

    fun readTracksFromHistory(): Array<Track>   //не забыть заменить на List

    fun clearHistory()

}