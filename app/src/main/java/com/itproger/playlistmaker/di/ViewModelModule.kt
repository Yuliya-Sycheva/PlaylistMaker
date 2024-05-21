package com.itproger.playlistmaker.di

import android.util.Log
import com.itproger.playlistmaker.search.ui.view_model.TracksSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<TracksSearchViewModel> {
        Log.d("TEST", "viewModel_Module")
        TracksSearchViewModel(trackInteractor = get())
    }
}