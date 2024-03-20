package com.itproger.playlistmaker.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class TrackDto {
    @Parcelize
    data class Track(
        val trackId: Int,
        val trackName: String,
        val artistName: String,
        val trackTimeMillis: Long,
        val artworkUrl100: String,
        val collectionName: String,
        val releaseDate: String,
        val primaryGenreName: String,
        val country: String,
        val previewUrl: String
    ) : Parcelable
}