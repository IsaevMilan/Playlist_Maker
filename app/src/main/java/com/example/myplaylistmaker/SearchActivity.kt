package com.example.myplaylistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    companion object {
        private const val INPUT_TEXT = "input_text"
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

    private val BASE_URL = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val mediaService = retrofit.create(ApiInterface::class.java)


    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: FrameLayout
    private lateinit var placeholderLineErr: FrameLayout
    private lateinit var moviesList: RecyclerView
    private lateinit var clearButton: ImageView
    private lateinit var updateButton: Button
    private lateinit var backButt: ImageView
    private val media = ArrayList<MediaData>()
    private val mediaAdapter = MediaAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        queryInput = findViewById(R.id.inputEditText)
        placeholderMessage = findViewById(R.id.placeholdSearchError)
        placeholderLineErr = findViewById(R.id.placeholdLineError)
        clearButton = findViewById(R.id.clearIcon)
        updateButton = findViewById(R.id.updateButton)
        backButt = findViewById(R.id.arrowBack3)
        moviesList = findViewById(R.id.rvTracks)
        mediaAdapter.media = media

        backButt.setOnClickListener {
            onBackPressed()
        }

        clearButton.setOnClickListener {
            queryInput.setText("")
            val keyboard =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(queryInput.windowToken, 0) // скрыть клавиатуру
            queryInput.clearFocus()
        }

        moviesList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        moviesList.adapter = mediaAdapter

        fun requestToServer() {
            moviesList.visibility = View.VISIBLE
            placeholderMessage.visibility = View.GONE
            placeholderLineErr.visibility = View.GONE
            if (queryInput.text.isNotEmpty()) {
                mediaService.findMedia(queryInput.text.toString()).enqueue(/* callback = */
                    object :
                        Callback<MediaResponse> {
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
                                    moviesList.visibility = View.GONE
                                    placeholderMessage.visibility = View.VISIBLE

                                }
                            } else {
                                media.clear()
                                moviesList.visibility = View.GONE
                                placeholderLineErr.visibility = View.VISIBLE

                            }
                        }

                        override fun onFailure(
                            call: Call<MediaResponse>,
                            t: Throwable
                        ) {
                            media.clear()
                            moviesList.visibility = View.GONE
                            placeholderLineErr.visibility = View.VISIBLE
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
                    clearButton.visibility = View.GONE
                } else {
                    clearButton.visibility = View.VISIBLE
                }

            }

            override fun afterTextChanged(s: Editable?) {}
        }
        queryInput.addTextChangedListener(simpleTextWatcher)
    }


//    private fun showMessage(text: FrameLayout, additionalMessage: String) {
//        if (text.isNotEmpty()) {
//            placeholderMessage.visibility = View.VISIBLE
//            media.clear()
//            mediaAdapter.notifyDataSetChanged()
//            placeholderMessage.FrameL
//            if (additionalMessage.isNotEmpty()) {
//                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
//                    .show()
//            }
//        } else {
//            placeholderMessage.visibility = View.GONE
//        }
//    }
 }








