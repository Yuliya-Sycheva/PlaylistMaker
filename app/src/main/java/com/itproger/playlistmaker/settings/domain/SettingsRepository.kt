package com.itproger.playlistmaker.settings.domain

import com.itproger.playlistmaker.settings.domain.model.ThemeSettings

interface SettingsRepository {

    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}