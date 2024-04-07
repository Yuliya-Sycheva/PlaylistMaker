package com.itproger.playlistmaker.settings.domain.impl

import com.itproger.playlistmaker.settings.data.SettingsRepository
import com.itproger.playlistmaker.settings.domain.SettingsInteractor
import com.itproger.playlistmaker.settings.domain.model.ThemeSettings

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository
) : SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
       return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(isChecked: Boolean) {
        return settingsRepository.updateThemeSetting(ThemeSettings(isChecked)) ///change
    }
}