package com.itproger.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.itproger.playlistmaker.R
import com.itproger.playlistmaker.databinding.FragmentSearchBinding
import com.itproger.playlistmaker.player.ui.PlayerActivity
import com.itproger.playlistmaker.search.domain.models.Track
import com.itproger.playlistmaker.search.ui.adapters.TrackAdapter
import com.itproger.playlistmaker.search.ui.models.SearchScreenState
import com.itproger.playlistmaker.search.ui.view_model.TracksSearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val CLICKED_TRACK: String = "clicked_track"
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<TracksSearchViewModel>()

    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed = true
    private var textWatcher: TextWatcher? = null

    private lateinit var searchAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    private val historyTrackClickListener: (Track) -> Unit =
        { clickedTrack ->
            openPlayer(clickedTrack)
        }

    private val currentTrackClickListener: (Track) -> Unit =
        { clickedTrack ->
            openPlayer(clickedTrack)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("TEST", "onCreate_start")
        viewModel.observeState().observe(viewLifecycleOwner) {
            Log.d("TEST", "render")
            render(it)
        }

        binding.historyLayout.isVisible = false

        searchAdapter = TrackAdapter(currentTrackClickListener)
        historyAdapter = TrackAdapter(historyTrackClickListener)

        binding.tracksSearchList.adapter = searchAdapter
        binding.tracksHistoryList.adapter = historyAdapter

        binding.tracksSearchList.layoutManager = LinearLayoutManager(requireContext())
        binding.tracksHistoryList.layoutManager = LinearLayoutManager(requireContext())

        binding.query.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchTrack(binding.query.text.toString())
            }
            false
        }

        binding.query.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.query.text.isEmpty()) {
                val historyTracks = viewModel.readTracksFromHistory().toMutableList()
                updateHistoryList(historyTracks)
                binding.historyLayout.isVisible =
                    historyTracks.isNotEmpty()
            } else {
                binding.historyLayout.isVisible = false
            }
        }

        //при нажатии на крестик
        binding.clearIcon.setOnClickListener {
            binding.query.setText("")
            binding.query.clearFocus()
            searchAdapter.trackList = mutableListOf()
            viewModel.onClearIconClick()
            historyAdapter.notifyDataSetChanged()
            hidePlaceholdersAndUpdateBtn()
            val imm =
                requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager   /////
            imm.hideSoftInputFromWindow(binding.query.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.isVisible = clearButtonVisibility(s)
                if (binding.query.hasFocus() && s?.isEmpty() == true) {
                    binding.historyLayout.isVisible
                } else {
                    hideHistory()
                }
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    searchAdapter.trackList.clear()
                    binding.tracksSearchList.adapter?.notifyDataSetChanged()
                    hidePlaceholdersAndUpdateBtn()
                }
            }
        }

        binding.query.addTextChangedListener(simpleTextWatcher)
        savedInstanceState?.getString(SearchFragment.SEARCH_TEXT)
            ?.let {
                binding.query.setText(it)
            }

        binding.updateButton.setOnClickListener {
            viewModel.searchTrack(binding.query.text.toString())
        }

        binding.cleanHistory.setOnClickListener {
            val historyTracks = viewModel.readTracksFromHistory().toMutableList()
            historyTracks.clear()
            viewModel.clearHistory()
            binding.historyLayout.isVisible = false
            historyAdapter.notifyDataSetChanged()
        }
    } // конец onViewCreated()

    override fun onResume() {
        super.onResume()
        val historyTracks = viewModel.readTracksFromHistory().toMutableList()
        updateHistoryList(historyTracks)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openPlayer(clickedTrack: Track) {   //???????????????????????????
        if (clickDebounce()) {
            viewModel.onClickedTrack(listOf(clickedTrack))
            historyAdapter.notifyDataSetChanged()
            //было:
//            val playerIntent = Intent(this, PlayerActivity::class.java)
//            playerIntent.putExtra(SearchFragment.CLICKED_TRACK, clickedTrack)
//            startActivity(playerIntent)

            //через Intent:
//            val playerIntent = Intent(requireActivity(), PlayerActivity::class.java)
//            playerIntent.putExtra(SearchFragment.CLICKED_TRACK, clickedTrack)
//            startActivity(playerIntent)

            findNavController().navigate(
                R.id.action_searchFragment_to_playerActivity,
                bundleOf(CLICKED_TRACK to clickedTrack)
            )
        }
    }

    //кнопка "Крестик"
    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { binding.query.removeTextChangedListener(it) }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, SearchFragment.CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Loading -> showLoading()
            is SearchScreenState.Content -> {
                searchAdapter.trackList = state.tracks
                showContent()
            }

            is SearchScreenState.Error -> showError(state.errorMessage)
            is SearchScreenState.Empty -> showEmpty()   //(state.message)
            is SearchScreenState.History -> showHistory()
            else -> {}
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBar.isVisible = true
            historyLayout.isVisible = false
            updateButton.isVisible = false
            placeholderImage.isVisible = false
            placeholderMessage.isVisible = false
        }
    }

    private fun showError(errorMessage: String) {
        with(binding) {
            progressBar.isVisible = false
            historyLayout.isVisible = false
            updateButton.isVisible = true
            placeholderImage.isVisible = true
            placeholderMessage.isVisible = true

            placeholderImage.setImageResource(R.drawable.something_went_wrong)
            placeholderMessage.text = errorMessage
        }
    }

    private fun showEmpty() {
        with(binding) {
            progressBar.isVisible = false
            historyLayout.isVisible = false
            updateButton.isVisible = false
            placeholderImage.isVisible = true
            placeholderMessage.isVisible = true

            placeholderImage.setImageResource(R.drawable.nothing_found)
            placeholderMessage.setText(R.string.nothing_found)
        }
    }

    private fun showContent() {
        with(binding) {
            tracksSearchList.adapter?.notifyDataSetChanged()

            progressBar.isVisible = false
            historyLayout.isVisible = false
            updateButton.isVisible = false
            placeholderImage.isVisible = false
            placeholderMessage.isVisible = false
        }
        hideHistory()
    }

    private fun hideHistory() {
        with(binding) {
            tracksHistoryList.isVisible = false
            youWereLookingFor.isVisible = false
            cleanHistory.isVisible = false
        }
    }

    private fun hidePlaceholdersAndUpdateBtn() {
        with(binding) {
            placeholderMessage.isVisible = false
            placeholderImage.isVisible = false
            updateButton.isVisible = false
        }
    }

    private fun showHistory() {
        with(binding) {
            tracksHistoryList.isVisible = true
            youWereLookingFor.isVisible = true
            cleanHistory.isVisible = true
        }
    }

    private fun updateHistoryList(historyTracks: MutableList<Track>) {
        historyAdapter.setData(historyTracks)
        historyAdapter.notifyDataSetChanged()
    }
}