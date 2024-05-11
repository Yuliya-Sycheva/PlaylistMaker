package com.itproger.playlistmaker.search.data


import android.util.Log
import com.itproger.playlistmaker.search.NetworkClient
import com.itproger.playlistmaker.search.data.dto.TrackRequest
import com.itproger.playlistmaker.search.data.dto.TrackResponse
import com.itproger.playlistmaker.search.domain.api.TrackRepository
import com.itproger.playlistmaker.search.domain.models.Track
import com.itproger.playlistmaker.search.data.preferences.SharedPreferencesSearchClientImpl
import com.itproger.playlistmaker.utils.GeneralConstants
import com.itproger.playlistmaker.utils.Resource

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val sharedPreferencesSearchClient: SharedPreferencesSearchClientImpl
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
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.collectionName,
                        it.releaseDate,
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
        sharedPreferencesSearchClient.saveTrackToHistory(track)
    }

    override fun readTracksFromHistory(): Array<Track> {
        return sharedPreferencesSearchClient.readTracksFromHistory()
    }

    override fun clearHistory() {
        sharedPreferencesSearchClient.clearHistory()
    }
}