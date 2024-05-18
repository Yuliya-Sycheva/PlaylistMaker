package com.itproger.playlistmaker.settings.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.itproger.playlistmaker.databinding.ActivitySettingsBinding
import com.itproger.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModelSettings: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelSettings = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        //Текущая тема:
        viewModelSettings.getThemeLiveData().observe(this) { isChecked ->
            binding.themeSwitcher.isChecked = isChecked
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.termsOfViews.setOnClickListener {
            viewModelSettings.clickOpenTerms()
        }

        binding.support.setOnClickListener {
            viewModelSettings.clickOpenSupport()
        }

        binding.share.setOnClickListener {
            viewModelSettings.clickShareApp()
        }

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
            viewModelSettings.clickSwitchTheme(isChecked)
        }
    }
}