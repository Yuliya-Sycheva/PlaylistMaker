package com.itproger.playlistmaker.di

import android.util.Log
import com.itproger.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.itproger.playlistmaker.player.domain.repository.PlayerRepository
import com.itproger.playlistmaker.search.data.TrackRepositoryImpl
import com.itproger.playlistmaker.search.domain.api.TrackRepository
import org.koin.dsl.factory
import org.koin.dsl.module

val repositoryModule = module {
    single<TrackRepository> {
        Log.d("TEST", "TrackRepository_Module")
        TrackRepositoryImpl(networkClient = get(), sharedPreferencesSearchHistoryStorage = get())
    }

    factory <PlayerRepository> {
        Log.d("TEST", "PlayerRepository_Module")
        PlayerRepositoryImpl(mediaPlayer = get())
    }
}