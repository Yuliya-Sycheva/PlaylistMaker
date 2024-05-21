package com.itproger.playlistmaker.di

import android.util.Log
import com.itproger.playlistmaker.search.domain.api.TrackInteractor
import com.itproger.playlistmaker.search.domain.impl.TrackInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    factory <TrackInteractor> {
        Log.d("TEST", "TrackInteractor_Module")
        TrackInteractorImpl(repository = get(), executor = get())
    }
}