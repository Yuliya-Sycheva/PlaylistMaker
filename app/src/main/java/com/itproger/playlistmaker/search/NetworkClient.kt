package com.itproger.playlistmaker.search

import com.itproger.playlistmaker.search.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}