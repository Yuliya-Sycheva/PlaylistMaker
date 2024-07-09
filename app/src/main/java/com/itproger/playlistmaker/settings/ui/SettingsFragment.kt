package com.itproger.playlistmaker.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.itproger.playlistmaker.databinding.FragmentSettingsBinding
import com.itproger.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModelSettings by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Текущая тема:
        viewModelSettings.getThemeLiveData().observe(viewLifecycleOwner) { isChecked ->
            binding.themeSwitcher.isChecked = isChecked
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}