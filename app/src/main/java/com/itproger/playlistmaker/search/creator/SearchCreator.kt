package com.itproger.playlistmaker.search.creator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.itproger.playlistmaker.search.data.TrackRepositoryImpl
import com.itproger.playlistmaker.search.data.network.RetrofitNetworkClient
import com.itproger.playlistmaker.search.domain.api.TrackInteractor
import com.itproger.playlistmaker.search.domain.api.TrackRepository
import com.itproger.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.itproger.playlistmaker.search.data.preferences.SharedPreferencesSearchClientImpl
import com.itproger.playlistmaker.utils.GeneralConstants

object SearchCreator {
    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(
            RetrofitNetworkClient(context), SharedPreferencesSearchClientImpl(
                context.getSharedPreferences(
                    GeneralConstants.SEARCH_HISTORY_PREFERENCES,
                    AppCompatActivity.MODE_PRIVATE
                )
            )
        )
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context))
    }
}