package com.itproger.playlistmaker.di

import com.itproger.playlistmaker.search.domain.api.TrackInteractor
import com.itproger.playlistmaker.search.domain.impl.TrackInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single <TrackInteractor> {
        TrackInteractorImpl(repository = get())
    }
}