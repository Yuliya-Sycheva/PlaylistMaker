package com.itproger.playlistmaker.utils

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.itproger.playlistmaker.di.dataModule
import com.itproger.playlistmaker.di.interactorModule
import com.itproger.playlistmaker.di.repositoryModule
import com.itproger.playlistmaker.di.viewModelModule
import com.itproger.playlistmaker.settings.domain.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {

    lateinit var settingsInteractor: SettingsInteractor
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(dataModule, repositoryModule, interactorModule, viewModelModule))
        }
      //  settingsInteractor = SettingsCreator.providesettingsInteractor(this)
        val settingsInteractor : SettingsInteractor by inject()

        darkTheme = settingsInteractor.getThemeSettings().darkTheme
        applyTheme(darkTheme)

    }

    fun applyTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}