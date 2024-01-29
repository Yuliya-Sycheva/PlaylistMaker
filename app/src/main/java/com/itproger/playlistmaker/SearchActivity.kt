package com.itproger.playlistmaker
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val searchHistoryPreferences = "playlist_maker_search_history_preferences"

    private var searchValue: String? = null

    private val iTunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    private val tracks = mutableListOf<Track>()

    private lateinit var queryInput: EditText
    private lateinit var tracksList: RecyclerView
    private lateinit var placeholderMessage: TextView
    private lateinit var placeholderImage: ImageView
    private lateinit var updateButton: Button
    private lateinit var tracksHistoryList: RecyclerView
    private lateinit var cleanHistoryButton: Button
    private lateinit var historyLayout: LinearLayout

    private lateinit var historyAdapter : TrackAdapter
    private lateinit var searchHistory: SearchHistory
    private lateinit var historyTracks: MutableList<Track>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val imageBack = findViewById<ImageView>(R.id.back)
        imageBack.setOnClickListener {
            finish()
        }
        placeholderMessage = findViewById(R.id.placeholderMessage)
        queryInput = findViewById(R.id.query)
        placeholderImage = findViewById(R.id.placeholderImage)
        updateButton = findViewById(R.id.updateButton)
        historyLayout = findViewById(R.id.historyLayout)

        historyLayout.visibility = View.GONE


        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack(queryInput.text.toString())
            }
            false
        }

        queryInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && queryInput.text.isEmpty()) {
                val historyTracks = searchHistory.readTracks().toMutableList()
                historyAdapter.setData(historyTracks)
                historyAdapter.notifyDataSetChanged()
                historyLayout.visibility = if (historyTracks.isNotEmpty()) View.VISIBLE else View.GONE
            } else {
                historyLayout.visibility = View.GONE
            }
        }

        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        clearButton.setOnClickListener {
            queryInput.setText("")
            queryInput.clearFocus()
            tracks.clear()
            tracksList.adapter?.notifyDataSetChanged()
            placeholderMessage.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            updateButton.visibility = View.GONE
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(queryInput.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchValue = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
                historyLayout.visibility =
                    if (queryInput.hasFocus() && s?.isEmpty() == true ) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        queryInput.addTextChangedListener(simpleTextWatcher)
        savedInstanceState?.getString(SEARCH_TEXT)?.let {
            queryInput.setText(it)
        }

        updateButton.setOnClickListener {
            searchTrack(queryInput.text.toString())
        }
        tracksList = findViewById(R.id.trackList)
        tracksList.layoutManager = LinearLayoutManager(this)

        val sharedPreferences = getSharedPreferences(searchHistoryPreferences, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)

        tracksList.adapter = TrackAdapter(tracks, searchHistory)

        tracksHistoryList = findViewById(R.id.tracksHistoryList)
        tracksHistoryList.layoutManager = LinearLayoutManager(this)

        historyTracks = searchHistory.readTracks().toMutableList()

         historyAdapter = TrackAdapter(historyTracks, searchHistory)
        tracksHistoryList.adapter = historyAdapter


        cleanHistoryButton = findViewById(R.id.cleanHistory)
        cleanHistoryButton.setOnClickListener {
            historyTracks.clear()
            sharedPreferences.edit().remove(HISTORY_TRACKS_LIST_KEY).apply()
            searchHistory.saveTrack(historyTracks)
            historyLayout.visibility = View.GONE
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
            placeholderMessage.visibility = View.VISIBLE
            historyLayout.visibility = View.GONE
            tracks.clear()
            tracksList.adapter?.notifyDataSetChanged()
            placeholderMessage.text = text
        } else {
            placeholderMessage.visibility = View.GONE
        }
    }

    private fun searchTrack(searchText: String) {
        iTunesService.search(searchText)
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>,

                    ) {
                    if (response.isSuccessful) {
                        val trackResponse = response.body()
                        if (trackResponse != null && trackResponse.results.isNotEmpty()) {
                            tracks.clear()
                            tracks.addAll(trackResponse.results)
                            tracksList.adapter?.notifyDataSetChanged()
                            showMessage("")
                        } else {
                            showMessage(getString(R.string.nothing_found))
                            historyLayout.visibility = View.GONE
                            placeholderImage.visibility = View.VISIBLE
                            placeholderImage.setImageResource(R.drawable.nothing_found)
                            updateButton.visibility = View.GONE
                        }
                    } else {
                        showMessage(getString(R.string.something_went_wrong))
                        historyLayout.visibility = View.GONE
                        placeholderImage.visibility = View.VISIBLE
                        updateButton.visibility = View.VISIBLE
                        placeholderImage.setImageResource(R.drawable.something_went_wrong)
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showMessage(getString(R.string.something_went_wrong))
                    tracks.clear()
                    tracksList.adapter?.notifyDataSetChanged()
                    placeholderImage.visibility = View.VISIBLE
                    updateButton.visibility = View.VISIBLE
                    historyLayout.visibility = View.GONE
                    placeholderImage.setImageResource(R.drawable.something_went_wrong)
                }
            })
    }

    private companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }
}