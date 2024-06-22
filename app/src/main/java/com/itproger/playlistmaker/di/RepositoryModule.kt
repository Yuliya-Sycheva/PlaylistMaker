package com.itproger.playlistmaker.di

import android.util.Log
import com.itproger.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.itproger.playlistmaker.player.domain.repository.PlayerRepository
import com.itproger.playlistmaker.search.data.TrackRepositoryImpl
import com.itproger.playlistmaker.search.domain.api.TrackRepository
import com.itproger.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.itproger.playlistmaker.settings.domain.SettingsRepository
import com.itproger.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.itproger.playlistmaker.sharing.domain.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.factory
import org.koin.dsl.module

val repositoryModule = module {
    single<TrackRepository> {
        Log.d("TEST", "TrackRepository_Module")
        TrackRepositoryImpl(networkClient = get(), sharedPreferencesSearchHistoryStorage = get())
    }

    factory<PlayerRepository> {
        Log.d("TEST", "PlayerRepository_Module")
        PlayerRepositoryImpl(mediaPlayer = get())
    }

    single<SettingsRepository> {
        Log.d("TEST", "SettingsRepository_Module")
        SettingsRepositoryImpl(sharedPrefs = get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }
}