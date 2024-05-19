package com.itproger.playlistmaker.di

import com.itproger.playlistmaker.search.data.TrackRepositoryImpl
import com.itproger.playlistmaker.search.domain.api.TrackRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TrackRepository> {
        TrackRepositoryImpl(networkClient = get(), sharedPreferencesSearchHistoryStorage = get())
    }
}