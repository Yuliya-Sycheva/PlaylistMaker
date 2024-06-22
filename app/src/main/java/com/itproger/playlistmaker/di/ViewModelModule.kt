package com.itproger.playlistmaker.di

import android.util.Log
import com.itproger.playlistmaker.player.ui.view_model.PlayerViewModel
import com.itproger.playlistmaker.search.ui.view_model.TracksSearchViewModel
import com.itproger.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<TracksSearchViewModel> {
        Log.d("TEST", "viewModel_Module")
        TracksSearchViewModel(trackInteractor = get())
    }

    viewModel<PlayerViewModel>() {
        Log.d("TEST", "PlayerViewModel_Module")
        PlayerViewModel(playerInteractor = get())
    }

    viewModel<SettingsViewModel>() {
        Log.d("TEST", "SettingsViewModel_Module")
        SettingsViewModel(sharingInteractor = get(), settingsInteractor = get())
    }
}