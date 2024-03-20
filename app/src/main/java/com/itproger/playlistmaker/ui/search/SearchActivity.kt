package com.itproger.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.itproger.playlistmaker.data.network.ITunesApi
import com.itproger.playlistmaker.R
import com.itproger.playlistmaker.SearchHistory
import com.itproger.playlistmaker.domain.models.Track
import com.itproger.playlistmaker.ui.TrackAdapter
import com.itproger.playlistmaker.data.dto.TrackResponse
import com.itproger.playlistmaker.databinding.ActivitySearchBinding
import com.itproger.playlistmaker.ui.player.PlayerActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_HISTORY_PREFERENCES = "playlist_maker_search_history_preferences"
        const val CLICKED_TRACK: String = "clicked_track"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable { searchTrack() }

    private lateinit var binding: ActivitySearchBinding

    private var searchValue: String? = null

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    private val tracks = mutableListOf<Track>()

    private lateinit var historyAdapter: TrackAdapter
    private lateinit var searchHistory: SearchHistory
    private lateinit var historyTracks: MutableList<Track>

    private val historyTrackClickListener: (Track) -> Unit = { clickedTrack ->
        if (clickDebounce()) {
            val playerIntent = Intent(this, PlayerActivity::class.java)
            playerIntent.putExtra(CLICKED_TRACK, clickedTrack)
            startActivity(playerIntent)
        }
    }

    private val currentTrackClickListener: (Track) -> Unit = { clickedTrack ->
        if (clickDebounce()) {
            searchHistory.saveTrack(listOf(clickedTrack))
            val playerIntent = Intent(this, PlayerActivity::class.java)
            playerIntent.putExtra(CLICKED_TRACK, clickedTrack)
            startActivity(playerIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }

        binding.historyLayout.visibility = View.GONE


        binding.query.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handler.removeCallbacks(searchRunnable)
                searchTrack()
            }
            false
        }

        binding.query.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.query.text.isEmpty()) {
                val historyTracks = searchHistory.readTracks().toMutableList()
                historyAdapter.setData(historyTracks)
                historyAdapter.notifyDataSetChanged()
                binding.historyLayout.visibility =
                    if (historyTracks.isNotEmpty()) View.VISIBLE else View.GONE
            } else {
                binding.historyLayout.visibility = View.GONE
            }
        }

        binding.clearIcon.setOnClickListener {
            binding.query.setText("")
            binding.query.clearFocus()
            tracks.clear()
            binding.trackList.adapter?.notifyDataSetChanged()
            binding.placeholderMessage.visibility = View.GONE
            binding.placeholderImage.visibility = View.GONE
            binding.updateButton.visibility = View.GONE
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.query.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchValue = s.toString()
                binding.clearIcon.visibility = clearButtonVisibility(s)
                binding.historyLayout.visibility =
                    if (binding.query.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    tracks.clear()
                    binding.trackList.adapter?.notifyDataSetChanged()
                    binding.placeholderMessage.visibility = View.GONE
                    binding.placeholderImage.visibility = View.GONE
                    binding.updateButton.visibility = View.GONE
                }
            }
        }

        binding.query.addTextChangedListener(simpleTextWatcher)
        savedInstanceState?.getString(SEARCH_TEXT)?.let {
            binding.query.setText(it)
        }

        binding.updateButton.setOnClickListener {
            searchTrack()
        }
        binding.trackList.layoutManager = LinearLayoutManager(this)

        val sharedPreferences =
            getSharedPreferences(SEARCH_HISTORY_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)

        binding.trackList.adapter = TrackAdapter(tracks, currentTrackClickListener)

        binding.tracksHistoryList.layoutManager = LinearLayoutManager(this)

        historyTracks = searchHistory.readTracks().toMutableList()

        historyAdapter = TrackAdapter(historyTracks, historyTrackClickListener)
        binding.tracksHistoryList.adapter = historyAdapter


        binding.cleanHistory.setOnClickListener {
            historyTracks.clear()
            searchHistory.clearTracks()
            searchHistory.saveTrack(historyTracks)
            binding.historyLayout.visibility = View.GONE
            historyAdapter.notifyDataSetChanged()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchValue = savedInstanceState.getString(SEARCH_TEXT)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


    private fun showMessage(text: String) {
        if (text.isNotEmpty()) {
            binding.placeholderMessage.visibility = View.VISIBLE
            binding.historyLayout.visibility = View.GONE
            tracks.clear()
            binding.trackList.adapter?.notifyDataSetChanged()
            binding.placeholderMessage.text = text
        } else {
            binding.placeholderMessage.visibility = View.GONE
        }
    }

    private fun searchTrack() {
        if (binding.query.text.toString().isBlank()) {  // чтобы после крестика не выскакивало, что ничего не нашлось
            return
        }
        binding.historyLayout.visibility = View.GONE
        binding.placeholderImage.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        showMessage("")
        iTunesService.search(binding.query.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>,
                ) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val trackResponse = response.body()
                        Log.d("TRANSLATION_LOG", "Status code: ${response.code()}")
                        if (trackResponse != null && trackResponse.results.isNotEmpty()) {
                            tracks.clear()
                            tracks.addAll(trackResponse.results)
                            binding.trackList.adapter?.notifyDataSetChanged()
                            showMessage("")

                            hideHistory()

                            // Обновление истории поиска
                            val historyTracks = searchHistory.readTracks().toMutableList()
                            historyAdapter.setData(historyTracks)
                            historyAdapter.notifyDataSetChanged()
                            binding.historyLayout.visibility =
                                if (historyTracks.isNotEmpty()) View.VISIBLE else View.GONE

                        } else {
                            showMessage(getString(R.string.nothing_found))
                            binding.historyLayout.visibility = View.GONE
                            binding.placeholderImage.visibility = View.VISIBLE
                            binding.placeholderImage.setImageResource(R.drawable.nothing_found)
                            binding.updateButton.visibility = View.GONE
                        }
                    } else {
                        showMessage(getString(R.string.something_went_wrong))
                        binding.historyLayout.visibility = View.GONE
                        binding.placeholderImage.visibility = View.VISIBLE
                        binding.updateButton.visibility = View.VISIBLE
                        binding.placeholderImage.setImageResource(R.drawable.something_went_wrong)
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    Log.d("TRANSLATION_LOG", "Бяда")
                    showMessage(getString(R.string.something_went_wrong))
                    tracks.clear()
                    binding.progressBar.visibility = View.GONE
                    binding.trackList.adapter?.notifyDataSetChanged()
                    binding.placeholderImage.visibility = View.VISIBLE
                    binding.updateButton.visibility = View.VISIBLE
                    binding.historyLayout.visibility = View.GONE
                    binding.placeholderImage.setImageResource(R.drawable.something_went_wrong)
                }
            })
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun hideHistory() {
        with(binding) {
            tracksHistoryList.visibility = View.GONE
            youWereLookingFor.visibility = View.GONE
            cleanHistory.visibility = View.GONE
        }
    }
}