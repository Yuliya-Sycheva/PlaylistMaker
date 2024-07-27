package com.itproger.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale

object TrackTimeConverter {
    fun milsToMinSec(milliseconds: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(milliseconds)
    }

    fun setTrackYear(year: String?) : String {
        return year?.substring(0, 4) ?: ""
    }
}