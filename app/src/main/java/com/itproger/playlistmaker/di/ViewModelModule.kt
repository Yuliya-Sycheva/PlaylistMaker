package com.itproger.playlistmaker.di

import com.itproger.playlistmaker.library.ui.view_model.FavoriteTracksViewModel
import com.itproger.playlistmaker.library.ui.view_model.PlaylistsViewModel
import com.itproger.playlistmaker.player.ui.view_model.PlayerViewModel
import com.itproger.playlistmaker.search.ui.view_model.TracksSearchViewModel
import com.itproger.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<TracksSearchViewModel> {
        TracksSearchViewModel(trackInteractor = get())
    }

    viewModel<PlayerViewModel>() {
        PlayerViewModel(playerInteractor = get())
    }

    viewModel<SettingsViewModel>() {
        SettingsViewModel(sharingInteractor = get(), settingsInteractor = get())
    }

    viewModel {
        FavoriteTracksViewModel()
    }

    viewModel {
        PlaylistsViewModel()
    }
}