package com.itproger.playlistmaker.di

import android.util.Log
import com.itproger.playlistmaker.search.data.TrackRepositoryImpl
import com.itproger.playlistmaker.search.domain.api.TrackRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TrackRepository> {
        Log.d("TEST", "TrackRepository_Module")
        TrackRepositoryImpl(networkClient = get(), sharedPreferencesSearchHistoryStorage = get())
    }
}