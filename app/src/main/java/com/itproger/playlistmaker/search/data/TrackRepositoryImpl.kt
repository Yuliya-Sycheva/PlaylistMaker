package com.itproger.playlistmaker.search.data


import android.util.Log
import com.itproger.playlistmaker.search.NetworkClient
import com.itproger.playlistmaker.search.data.dto.TrackRequest
import com.itproger.playlistmaker.search.data.dto.TrackResponse
import com.itproger.playlistmaker.search.data.preferences.SearchHistoryStorage
import com.itproger.playlistmaker.search.data.preferences.SharedPreferencesSearchHistoryStorage
import com.itproger.playlistmaker.search.domain.api.TrackRepository
import com.itproger.playlistmaker.search.domain.models.Track
import com.itproger.playlistmaker.utils.GeneralConstants
import com.itproger.playlistmaker.utils.Resource
import com.itproger.playlistmaker.utils.TrackTimeConverter

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val sharedPreferencesSearchHistoryStorage: SearchHistoryStorage
) : TrackRepository {

    override fun searchTracks(text: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackRequest(text))
        return when (response.resultCode) {
            GeneralConstants.NO_INTERNET -> {
                Log.d("TEST", "-1")
                Resource.Error(GeneralConstants.CHECK_INTERNET)
            }

            GeneralConstants.SUCCESS_INTERNET -> {
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
                Log.d("TEST", GeneralConstants.SERVER_ERROR)
                Resource.Error(GeneralConstants.SERVER_ERROR)
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