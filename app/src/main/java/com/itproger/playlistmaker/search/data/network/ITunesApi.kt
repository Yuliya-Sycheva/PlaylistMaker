package com.itproger.playlistmaker.search.data.network

import com.itproger.playlistmaker.search.data.dto.TrackResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): TrackResponse
}