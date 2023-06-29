package com.example.myplaylistmaker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



 class SearchActivity : AppCompatActivity(), MediaAdapter.MediaClickListener {

    private lateinit var backButt: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: LinearLayout
    private lateinit var placeholderLineErr: LinearLayout
    private lateinit var updateButton: Button
    private lateinit var moviesList: RecyclerView
    private lateinit var searchHistory: NestedScrollView
    private lateinit var historyRecycler: RecyclerView
    private lateinit var searchHistoryObj: SearchHistory
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var clearHistoryButton: Button
    private val media= ArrayList<MediaData>()
    private val historyAdapter= SearchHistoryAdapter(this)

    private val mediaInHistory = ArrayList<MediaData>()

     val mediaAdapter=MediaAdapter(this)

    companion object {
        private const val INPUT_TEXT = "input_text"
        private const val BASE_URL = "https://itunes.apple.com"
    }

    private var saveText = ""

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TEXT, saveText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        saveText = savedInstanceState.getString(INPUT_TEXT, "")
    }


    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val mediaService = retrofit.create(ApiInterface::class.java)

    override fun onTrackClick(track: MediaData) {
        searchHistoryObj.addNewTrack(track)
    }

    // метод дессириализует массив объектов Fact (в Shared Preference они хранятся в виде json строки)
    private fun createTrackListFromJson(json: String?): Array<MediaData> {
        return Gson().fromJson(json, Array<MediaData>::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        historyAdapter.searchHistory = mediaInHistory

        queryInput = findViewById(R.id.inputEditText)
        placeholderMessage = findViewById(R.id.SearchErrorLayout)
        placeholderLineErr = findViewById(R.id.LineErrorLayout)
        clearButton = findViewById(R.id.clearIcon)
        updateButton = findViewById(R.id.updateButton)
        backButt = findViewById(R.id.arrowBack3)
        moviesList = findViewById(R.id.rvTracks)
        searchHistory = findViewById(R.id.historyScrollView)
        historyRecycler = findViewById(R.id.historyRecycler)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
        sharedPreferences = getSharedPreferences(TRACKS_PREFERENCES, MODE_PRIVATE)
        searchHistoryObj = SearchHistory(sharedPreferences)

        mediaInHistory.addAll(searchHistoryObj.searchedTrackList)
        if (mediaInHistory.isEmpty()) {
            searchHistory.visibility = GONE
        }


        listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == TRACKS_LIST_KEY) {
                val media = sharedPreferences?.getString(TRACKS_LIST_KEY, null)
                if (media != null) {
                    mediaInHistory.clear()
                    mediaInHistory.addAll(createTrackListFromJson(media))
                    historyAdapter.notifyDataSetChanged()
                }
            }
        }


        clearHistoryButton.setOnClickListener {
            searchHistoryObj.clearHistory()
            mediaInHistory.clear()
            searchHistory.visibility = GONE
            historyAdapter.notifyDataSetChanged()
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        queryInput.setOnFocusChangeListener { v, hasFocus ->
            searchHistory.visibility = if (hasFocus && queryInput.text.isEmpty() && mediaInHistory.isNotEmpty())
                View.VISIBLE else GONE
        }


        backButt.setOnClickListener {
            onBackPressed()
        }

        clearButton.setOnClickListener {
            queryInput.setText("")
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(queryInput.windowToken, 0) // скрыть клавиатуру
            queryInput.clearFocus()
            media.clear()
            moviesList.visibility = GONE
            mediaAdapter.notifyDataSetChanged()
            if (mediaInHistory.isEmpty()) {
                searchHistory.visibility= GONE

            }
        }

        mediaAdapter.media= media
        moviesList.adapter = mediaAdapter
        historyRecycler.adapter = historyAdapter

        fun requestToServer() {
            moviesList.visibility = View.VISIBLE

            if (queryInput.text.isNotEmpty()) {
                mediaService.findMedia(queryInput.text.toString()).enqueue(/* callback = */
                    object : Callback<MediaResponse> {
                        override fun onResponse(
                            call: Call<MediaResponse>,
                            response: Response<MediaResponse>) {
                            if (response.code() == 200) {
                                media.clear()
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    media.addAll(response.body()?.results!!)
                                    mediaAdapter.notifyDataSetChanged()
                                }

                                else {
                                    media.clear()
                                    moviesList.visibility = GONE
                                    placeholderMessage.visibility = View.VISIBLE
                                    placeholderLineErr.visibility = GONE

                                }
                            } else {
                                media.clear()
                                moviesList.visibility = GONE
                                placeholderMessage.visibility= GONE
                                placeholderLineErr.visibility = View.VISIBLE
                                updateButton.visibility = View.VISIBLE
                            }
                        }

                        override fun onFailure(
                            call: Call<MediaResponse>,
                            t: Throwable
                        ) {
                            media.clear()
                            moviesList.visibility = GONE
                            placeholderLineErr.visibility = View.VISIBLE
                            updateButton.visibility = View.VISIBLE
                            placeholderMessage.visibility= GONE
                            // метод вызывается если не получилось установить соединение с сервером
                        }


                    })

            }

        }
        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                requestToServer()
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ
                true
            }
            false
        }

        updateButton.setOnClickListener {
            requestToServer()
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
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
                        searchHistory.visibility=View.VISIBLE
                    } else {
                        searchHistory.visibility= GONE
                    }
                }
            }
        }
        queryInput.addTextChangedListener(simpleTextWatcher)
    }

 }
const val TRACKS_PREFERENCES = "tracks_preferences"
const val TRACKS_LIST_KEY = "key_for_tracks_list"







