package com.itproger.playlistmaker.search.data


import android.util.Log
import com.itproger.playlistmaker.search.NetworkClient
import com.itproger.playlistmaker.search.data.dto.TrackRequest
import com.itproger.playlistmaker.search.data.dto.TrackResponse
import com.itproger.playlistmaker.search.domain.api.TrackRepository
import com.itproger.playlistmaker.search.domain.models.Track
import com.itproger.playlistmaker.search.data.preferences.SharedPreferencesSearchClient
import com.itproger.playlistmaker.search.data.preferences.SharedPreferencesSearchClientImpl
import com.itproger.playlistmaker.utils.Resource

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val sharedPreferencesSearchClient: SharedPreferencesSearchClientImpl
) : TrackRepository {

    // private val dateFormat by lazy{ SimpleDateFormat("mm:ss", Locale.getDefault()) }  /////????????

    override fun searchTracks(text: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackRequest(text))
        return when (response.resultCode) {
            -1 -> {
                Log.d("TEST", "-1")
                Resource.Error("Проверьте подключение к интернету") // исправить!!!!!
            }

            200 -> {
                Resource.Success((response as TrackResponse).results.map {
                    Track(
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,  /////////////
                        //  dateFormat.format(it.trackTimeMillis),   /////????????
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
                Log.d("TEST", "Ошибка сервера")
                Resource.Error("Ошибка сервера")  // исправить!!!!!
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