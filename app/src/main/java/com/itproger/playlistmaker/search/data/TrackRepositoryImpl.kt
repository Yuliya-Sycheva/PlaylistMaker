package com.itproger.playlistmaker.search.data


import android.util.Log
import com.itproger.playlistmaker.search.NetworkClient
import com.itproger.playlistmaker.search.data.dto.TrackRequest
import com.itproger.playlistmaker.search.data.dto.TrackResponse
import com.itproger.playlistmaker.search.data.preferences.SearchHistoryStorage
import com.itproger.playlistmaker.search.domain.api.TrackRepository
import com.itproger.playlistmaker.search.domain.models.Track
import com.itproger.playlistmaker.utils.Resource
import com.itproger.playlistmaker.utils.TrackTimeConverter

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val sharedPreferencesSearchHistoryStorage: SearchHistoryStorage
) : TrackRepository {

    private companion object {
        const val CHECK_INTERNET = "Проблемы со связью \\n\\nЗагрузка не удалась. Проверьте подключение к интернету"
        const val NO_INTERNET = -1
        const val SUCCESS_INTERNET = 200
        const val SERVER_ERROR = "Ошибка сервера"
    }

    override fun searchTracks(text: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackRequest(text))
        return when (response.resultCode) {
            NO_INTERNET -> {
                Log.d("TEST", "-1")
                Resource.Error(CHECK_INTERNET)
            }

            SUCCESS_INTERNET -> {
                Resource.Success((response as TrackResponse).results.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        TrackTimeConverter.milsToMinSec(it.trackTimeMillis),
                        it.artworkUrl100,
                        it.collectionName,
                        TrackTimeConverter.setTrackYear(it.releaseDate),
                        it.primaryGenreName,
                        it.country,
                        it.previewUrl
                    )
                })
            }

            else -> {
                Log.d("TEST", SERVER_ERROR)
                Resource.Error(SERVER_ERROR)
            }
        }
    }

    override fun saveTrackToHistory(track: List<Track>) {
        sharedPreferencesSearchHistoryStorage.saveTrackToHistory(track)
    }

    override fun readTracksFromHistory(): List<Track> {
        return sharedPreferencesSearchHistoryStorage.readTracksFromHistory()
    }

    override fun clearHistory() {
        sharedPreferencesSearchHistoryStorage.clearHistory()
    }
}