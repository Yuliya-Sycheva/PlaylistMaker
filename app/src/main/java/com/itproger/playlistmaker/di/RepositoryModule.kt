package com.itproger.playlistmaker.di

import com.itproger.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.itproger.playlistmaker.player.domain.repository.PlayerRepository
import com.itproger.playlistmaker.search.data.TrackRepositoryImpl
import com.itproger.playlistmaker.search.domain.api.TrackRepository
import com.itproger.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.itproger.playlistmaker.settings.domain.SettingsRepository
import com.itproger.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.itproger.playlistmaker.sharing.domain.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<TrackRepository> {
        TrackRepositoryImpl(networkClient = get(), sharedPreferencesSearchHistoryStorage = get())
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(mediaPlayer = get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(sharedPrefs = get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }
}