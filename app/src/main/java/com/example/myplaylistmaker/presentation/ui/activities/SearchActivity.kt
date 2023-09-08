package com.example.myplaylistmaker.presentation.ui.activities

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.data.dto.TrackResponse
import com.example.myplaylistmaker.data.network.iTunesSearchAPI
import com.example.myplaylistmaker.domain.SearchHistory
import com.example.myplaylistmaker.domain.models.Track
import com.example.myplaylistmaker.presentation.ui.adapters.MediaAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SEARCH_SHARED_PREFS_KEY = "123"

class SearchActivity : AppCompatActivity() {


    private lateinit var backButton: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: LinearLayout
    private lateinit var placeholderLineErr: LinearLayout
    private lateinit var updateButton: Button
    private lateinit var mediaRecycler: RecyclerView
    private lateinit var searchHistory: NestedScrollView
    private lateinit var historyRecycler: RecyclerView
    private val searchHistoryObj = SearchHistory()
    private lateinit var clearHistoryButton: Button
    private val handler = Handler(Looper.getMainLooper())
    lateinit var progressBar: ProgressBar

    private val historyAdapter = MediaAdapter {
        startMediaPlayerAndUpdateHistory(it)
    }
    private val mediaAdapter = MediaAdapter {
        startMediaPlayerAndUpdateHistory(it)
    }

    private val BASE_URL = "https://itunes.apple.com"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val mediaService = retrofit.create(iTunesSearchAPI::class.java)


    private var saveText = ""

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TEXT, saveText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        saveText = savedInstanceState.getString(INPUT_TEXT, "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        mediaRecycler = findViewById(R.id.rvTracks)
        queryInput = findViewById(R.id.inputEditText)
        placeholderMessage = findViewById(R.id.SearchErrorLayout)
        placeholderLineErr = findViewById(R.id.LineErrorLayout)
        clearButton = findViewById(R.id.clearIcon)
        updateButton = findViewById(R.id.updateButton)
        backButton = findViewById(R.id.arrowBack3)
        searchHistory = findViewById(R.id.historyScrollView)
        historyRecycler = findViewById(R.id.historyRecycler)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
        progressBar = findViewById(R.id.progressBar)


        applicationContext.getSharedPreferences(SEARCH_SHARED_PREFS_KEY, MODE_PRIVATE)

        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = historyAdapter
        searchHistory.isGone = true

        queryInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus &&
                queryInput.text.isBlank() &&
                searchHistoryObj.trackHistoryList.isNotEmpty()
            ) {
                historyAdapter.setItems(searchHistoryObj.trackHistoryList)
                searchHistory.isVisible = true
            } else {
                searchHistory.isGone = true
            }
        }

        clearHistoryButton.setOnClickListener {
            searchHistoryObj.trackHistoryList.clear()
            searchHistory.visibility = GONE
        }

        backButton.setOnClickListener {
            onBackPressed()
        }

        clearButton.setOnClickListener {
            queryInput.setText("")
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(queryInput.windowToken, 0) // скрыть клавиатуру
            queryInput.clearFocus()
            mediaAdapter.setItems(emptyList())
            placeholderMessage.visibility = GONE
            placeholderLineErr.visibility = GONE
            mediaRecycler.visibility = GONE
            if (searchHistoryObj.trackHistoryList.isEmpty()) {
                searchHistory.visibility = GONE
                placeholderMessage.visibility = GONE

            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) = Unit

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int,
            ) {
                if (s.isNullOrEmpty()) {
                    clearButton.visibility = GONE
                } else {
                    clearButton.visibility = View.VISIBLE
                }

            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (queryInput.hasFocus() && s.isEmpty() && !historyRecycler.isEmpty()) {
                        searchHistory.visibility = View.VISIBLE
                    } else {
                        searchHistory.visibility = GONE
                    }
                }
            }
        }
        queryInput.addTextChangedListener(simpleTextWatcher)
        mediaRecycler.adapter = mediaAdapter
        historyRecycler.adapter = historyAdapter
        queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchDebounce()
                if (queryInput.hasFocus() &&
                    p0?.isEmpty() == true &&
                    searchHistoryObj.trackHistoryList.isNotEmpty()) {
                    searchHistory.visibility = View.VISIBLE
                } else {
                    searchHistory.visibility = GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        updateButton.setOnClickListener {
            placeholderLineErr.visibility = GONE
            requestToServer()
        }


    }

    private fun requestToServer() {
        if (queryInput.text.isNotEmpty()) {
            mediaRecycler.visibility = GONE
            placeholderLineErr.isGone = true
            progressBar.visibility = View.VISIBLE
            mediaService.search(queryInput.text.toString()).enqueue(/* callback = */
                object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>,
                    ) {
                        if (response.code() == 200) {
                            val results = response.body()?.results.orEmpty()
                            val isEmpty = results.isEmpty()
                            mediaRecycler.isGone = isEmpty
                            placeholderLineErr.isGone = isEmpty
                            placeholderMessage.isVisible = isEmpty
                            mediaAdapter.setItems(results)
                        } else {
                            mediaRecycler.visibility = GONE
                            placeholderMessage.visibility = GONE
                            placeholderLineErr.visibility = View.VISIBLE
                            updateButton.visibility = View.VISIBLE
                        }
                        progressBar.visibility = GONE
                    }

                    override fun onFailure(
                        call: Call<TrackResponse>,
                        t: Throwable,
                    ) {
                        mediaRecycler.visibility = GONE
                        placeholderLineErr.visibility = View.VISIBLE
                        updateButton.visibility = View.VISIBLE
                        placeholderMessage.visibility = GONE
                        progressBar.visibility = GONE
                        // метод вызывается если не получилось установить соединение с сервером
                    }
                })

        }

    }

    private fun searchDebounce() {
        val searchRunnable = Runnable { requestToServer() }
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun startMediaPlayerAndUpdateHistory(track: Track) {
        MediaPlayerActivity.startActivity(this, track)
        searchHistoryObj.editArray(track)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val INPUT_TEXT = "input_text"

    }

}








