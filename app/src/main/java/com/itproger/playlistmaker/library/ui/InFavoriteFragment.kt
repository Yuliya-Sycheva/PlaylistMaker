package com.itproger.playlistmaker.library.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.itproger.playlistmaker.databinding.FragmentInFavoriteBinding
import com.itproger.playlistmaker.library.ui.view_model.InFavoriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class InFavoriteFragment : Fragment() {

    companion object {

        fun newInstance() = InFavoriteFragment()
    }

    private val inFavoriteViewModel: InFavoriteViewModel by viewModel<InFavoriteViewModel>()

    private lateinit var binding: FragmentInFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInFavoriteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}