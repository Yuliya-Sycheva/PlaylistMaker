package com.itproger.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.itproger.playlistmaker.R
import com.itproger.playlistmaker.search.ui.adapters.TrackAdapter
import com.itproger.playlistmaker.databinding.ActivitySearchBinding
import com.itproger.playlistmaker.player.ui.PlayerActivity
import com.itproger.playlistmaker.search.domain.models.Track
import com.itproger.playlistmaker.search.view_model.TracksSearchViewModel
import com.itproger.playlistmaker.search.ui.models.SearchScreenState

class SearchActivity : AppCompatActivity() {

    private companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val CLICKED_TRACK: String = "clicked_track"
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var binding: ActivitySearchBinding

    private lateinit var viewModel: TracksSearchViewModel

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            TracksSearchViewModel.getViewModelFactory()
        )[TracksSearchViewModel::class.java]
        viewModel.observeState().observe(this) {
            render(it)
        }

        binding.historyLayout.visibility = View.GONE

        searchAdapter = TrackAdapter(currentTrackClickListener)
        historyAdapter = TrackAdapter(historyTrackClickListener)

        binding.tracksSearchList.adapter = searchAdapter
        binding.tracksHistoryList.adapter = historyAdapter

        binding.tracksSearchList.layoutManager = LinearLayoutManager(this)
        binding.tracksHistoryList.layoutManager = LinearLayoutManager(this)

        binding.back.setOnClickListener {
            finish()
        }

        binding.query.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchTrack(binding.query.text.toString())
            }
            false
        }

        binding.query.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.query.text.isEmpty()) {
                val historyTracks = viewModel.readTracksFromHistory().toMutableList()
                historyAdapter.setData(historyTracks)
                binding.historyLayout.visibility =
                    if (historyTracks.isNotEmpty()) View.VISIBLE else View.GONE
            } else {
                binding.historyLayout.visibility = View.GONE
            }
        }

        //при нажатии на крестик
        binding.clearIcon.setOnClickListener {
            binding.query.setText("")
            binding.query.clearFocus()
            searchAdapter.trackList = arrayListOf()
            hidePlaceholdersAndUpdateBtn()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.query.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
                binding.historyLayout.visibility =
                    if (binding.query.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
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
        savedInstanceState?.getString(SEARCH_TEXT)
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
            binding.historyLayout.visibility = View.GONE
            historyAdapter.notifyDataSetChanged()
        }
    } // конец onCreate()

    private fun openPlayer(clickedTrack: Track) {
        if (clickDebounce()) {
            viewModel.onClickedTrack(listOf(clickedTrack))   // странно как-то, что я передаю здесь List, а не track = ?
            val playerIntent = Intent(this, PlayerActivity::class.java)
            playerIntent.putExtra(CLICKED_TRACK, clickedTrack)
            startActivity(playerIntent)
        }
    }

    //кнопка "Крестик"
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { binding.query.removeTextChangedListener(it) }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
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
            is SearchScreenState.Empty -> showEmpty(state.message)
            else -> {}
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
            historyLayout.visibility = View.GONE
            updateButton.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
        }
    }

    private fun showError(errorMessage: String) {
        with(binding) {
            progressBar.visibility = View.GONE
            historyLayout.visibility = View.GONE
            updateButton.visibility = View.VISIBLE
            placeholderImage.visibility = View.VISIBLE
            placeholderMessage.visibility = View.VISIBLE

            placeholderImage.setImageResource(R.drawable.something_went_wrong)
            placeholderMessage.text = errorMessage
        }
    }

    private fun showEmpty(emptyMessage: String) {
        with(binding) {
            progressBar.visibility = View.GONE
            historyLayout.visibility = View.GONE
            updateButton.visibility = View.GONE
            placeholderImage.visibility = View.VISIBLE
            placeholderMessage.visibility = View.VISIBLE

            placeholderImage.setImageResource(R.drawable.nothing_found)
            placeholderMessage.text = emptyMessage
        }
    }

    private fun showContent() {
        with(binding) {
            tracksSearchList.adapter?.notifyDataSetChanged()

            progressBar.visibility = View.GONE
            historyLayout.visibility = View.GONE
            updateButton.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            placeholderMessage.visibility = View.GONE
        }
        hideHistory()
    }

    private fun hideHistory() {
        with(binding) {
            tracksHistoryList.visibility = View.GONE
            youWereLookingFor.visibility = View.GONE
            cleanHistory.visibility = View.GONE
        }
    }

    private fun hidePlaceholdersAndUpdateBtn() {
        with(binding) {
            placeholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            updateButton.visibility = View.GONE
        }
    }
}