package com.itproger.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale

object TrackTimeFormatter {
    private val dateFormat by lazy{ SimpleDateFormat("mm:ss", Locale.getDefault()) }
    fun formatTimeToString(time: Long): String {
        return dateFormat.format(time)
    }
}