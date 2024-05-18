package com.itproger.playlistmaker.settings.domain

import com.itproger.playlistmaker.settings.domain.model.ThemeSettings

interface SettingsInteractor {

    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(isChecked: Boolean)
}