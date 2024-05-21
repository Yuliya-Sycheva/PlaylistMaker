package com.itproger.playlistmaker.di

import android.util.Log
import com.itproger.playlistmaker.player.domain.interactor.PlayerInteractor
import com.itproger.playlistmaker.player.domain.interactor.impl.PlayerInteractorImpl
import com.itproger.playlistmaker.search.domain.api.TrackInteractor
import com.itproger.playlistmaker.search.domain.impl.TrackInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single <TrackInteractor> {
        Log.d("TEST", "TrackInteractor_Module")
        TrackInteractorImpl(repository = get(), executor = get())
    }

    factory <PlayerInteractor> {
        Log.d("TEST", "PlayerInteractor_Module")
        PlayerInteractorImpl(playerRepository = get())
    }
}