package com.itproger.playlistmaker.search.data.preferences

import com.itproger.playlistmaker.search.domain.models.Track

interface SharedPreferencesSearchClient {
    fun saveTrackToHistory(track: List<Track>)
    fun readTracksFromHistory(): Array<Track> //не забыть заменить на List
    fun clearHistory()
}