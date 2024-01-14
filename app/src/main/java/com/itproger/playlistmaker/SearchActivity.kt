package com.itproger.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    private var searchValue: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val imageBack = findViewById<ImageView>(R.id.back)
        imageBack.setOnClickListener {
            finish()
        }
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        clearButton.setOnClickListener {
            inputEditText.setText("")
            inputEditText.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(inputEditText.windowToken, 0)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchValue = s.toString()
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
        savedInstanceState?.getString(SEARCH_TEXT)?.let {
            inputEditText.setText(it)
        }

        val recycler = findViewById<RecyclerView>(R.id.trackList)
        recycler.layoutManager = LinearLayoutManager(this)
        val trackList = listOf(
            Track(
                getString(R.string.track_Smells_Like_Teen_Spirit),
                getString(R.string.artist_Nirvana),
                "5:01",
                getString(R.string.image_nirvana_url)
            ),
            Track(
                getString(R.string.track_Billie_Jean),
                getString(R.string.artist_Michael_Jackson),
                "4:35",
                getString(R.string.image_jackson_url)
            ),
            Track(
                getString(R.string.track_Stayin_Alive),
                getString(R.string.artist_Bee_Gees),
                "4:10",
                getString(R.string.image_bee_gees_url)
            ),
            Track(
                getString(R.string.track_Whole_Lotta_Love),
                getString(R.string.artist_Led_Zeppelin),
                "5:33",
                getString(R.string.image_led_zeppelin_url)
            ),
            Track(
                getString(R.string.track_Sweet_Child_O_Mine),
                getString(R.string.artist_Guns_N_Roses),
                "5:03",
                getString(R.string.image_guns_n_roses_url)
            )
        )
        recycler.adapter = TrackAdapter(trackList)
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

    private companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }
}