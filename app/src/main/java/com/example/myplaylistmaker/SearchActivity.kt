package com.example.myplaylistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SEARCH_SHARED_PREFS_KEY = "123"

 class SearchActivity : AppCompatActivity() {




    private lateinit var  backButton: ImageView
    private lateinit var  clearButton: ImageView
    private lateinit var  queryInput: EditText
    private lateinit var  placeholderMessage: LinearLayout
    private lateinit var  placeholderLineErr: LinearLayout
    private lateinit var  updateButton: Button
    private lateinit var  mediaRecycler: RecyclerView
    private lateinit var  searchHistory: NestedScrollView
    private lateinit var  historyRecycler: RecyclerView
    val searchHistoryObj= SearchHistory()
    private lateinit var  clearHistoryButton: Button
    private lateinit var mediaList: ArrayList<MediaData>
    private val mediaInHistory = ArrayList<MediaData>()
    private lateinit var  historyAdapter:MediaAdapter
    lateinit var  mediaAdapter:MediaAdapter
     private val handler = Handler(Looper.getMainLooper())
     private val searchRunnable = Runnable { (queryInput) }
     lateinit var progressBar: ProgressBar


    private val BASE_URL = "https://itunes.apple.com"
    private val retrofit: Retrofit = Retrofit.Builder()
         .baseUrl(BASE_URL)
         .addConverterFactory(GsonConverterFactory.create())
         .build()
    private val mediaService = retrofit.create(ApiInterface::class.java)


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


        mediaList = ArrayList()
        mediaAdapter = MediaAdapter(mediaList)
        historyAdapter = MediaAdapter(searchHistoryObj.trackHistoryList)
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


        mediaInHistory.addAll(searchHistoryObj.trackHistoryList)
        if (mediaInHistory.isEmpty()) {
            searchHistory.visibility = GONE
        }

        queryInput.setOnFocusChangeListener { v, hasFocus ->
            searchHistory.visibility = if (hasFocus && queryInput.text.isEmpty() && App.mediaHistoryList.isNotEmpty())
                View.VISIBLE else GONE
        }

        clearHistoryButton.setOnClickListener {
            App.mediaHistoryList.clear()
            mediaInHistory.clear()
            searchHistory.visibility = GONE
            historyAdapter.notifyDataSetChanged()
        }




        backButton.setOnClickListener {
            onBackPressed()
        }

        clearButton.setOnClickListener {
            queryInput.setText("")
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(queryInput.windowToken, 0) // скрыть клавиатуру
            queryInput.clearFocus()
            mediaList.clear()
            placeholderMessage.visibility= GONE
            placeholderLineErr.visibility = GONE
            mediaRecycler.visibility = GONE
            mediaAdapter.notifyDataSetChanged()
            if (mediaInHistory.isEmpty()) {
                searchHistory.visibility= GONE
                placeholderMessage.visibility= GONE

            }
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

        mediaAdapter.media= mediaList
        mediaRecycler.adapter = mediaAdapter
        historyRecycler.adapter = historyAdapter


        fun requestToServer() {

            mediaList.clear()



            if (queryInput.text.isNotEmpty()) {
                mediaRecycler.visibility = View.VISIBLE
                progressBar.visibility = View.VISIBLE
                mediaService.findMedia(queryInput.text.toString()).enqueue(/* callback = */
                    object : Callback<MediaResponse> {
                        override fun onResponse(
                            call: Call<MediaResponse>,
                            response: Response<MediaResponse>) {
                            if (response.code() == 200) {
                                mediaList.clear()
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    mediaList.addAll(response.body()?.results!!)
                                    mediaAdapter.notifyDataSetChanged()
                                }

                                else {
                                    mediaList.clear()
                                    mediaRecycler.visibility = GONE
                                    placeholderMessage.visibility = View.VISIBLE
                                    placeholderLineErr.visibility = GONE

                                }
                                progressBar.visibility = GONE
                            } else {
                                mediaList.clear()
                                mediaRecycler.visibility = GONE
                                placeholderMessage.visibility= GONE
                                placeholderLineErr.visibility = View.VISIBLE
                                updateButton.visibility = View.VISIBLE
                                progressBar.visibility = View.GONE
                            }
                        }

                        override fun onFailure(
                            call: Call<MediaResponse>,
                            t: Throwable
                        ) {
                            mediaList.clear()
                            mediaRecycler.visibility = GONE
                            placeholderLineErr.visibility = View.VISIBLE
                            updateButton.visibility = View.VISIBLE
                            placeholderMessage.visibility= GONE
                            progressBar.visibility = View.GONE
                            // метод вызывается если не получилось установить соединение с сервером
                        }


                    })

            }

        }
        val searchRunnable = Runnable { requestToServer() }


        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
        queryInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchDebounce()
                if (queryInput.hasFocus() && p0?.isEmpty() == true && App.mediaHistoryList.isNotEmpty()) {
                    searchHistory.visibility= View.VISIBLE
                } else {
                    searchHistory.visibility= GONE
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
     companion object {
         private const val SEARCH_DEBOUNCE_DELAY = 2000L
         private const val INPUT_TEXT = "input_text"

     }

 }








