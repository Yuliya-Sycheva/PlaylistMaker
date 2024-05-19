package com.itproger.playlistmaker.search.ui.models

import com.itproger.playlistmaker.search.domain.models.Track

sealed interface SearchScreenState {
    object Loading : SearchScreenState

    data class Content(
        val tracks: MutableList<Track>
    ) : SearchScreenState

    data class Error(
        val errorMessage: String
    ) : SearchScreenState

    object Empty : SearchScreenState

//    data class Empty(
//     val message: String
//    ) : SearchScreenState

    data class History(
        val tracks: MutableList<Track>
    ) : SearchScreenState
}