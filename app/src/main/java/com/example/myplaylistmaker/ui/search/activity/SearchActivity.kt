package com.example.myplaylistmaker.ui.search.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.ActivitySearchBinding
import com.example.myplaylistmaker.domain.search.models.Track
import com.example.myplaylistmaker.ui.player.activity.PlayerActivity
import com.example.myplaylistmaker.ui.search.adapter.TrackAdapter
import com.example.myplaylistmaker.ui.search.view_model_for_activity.SearchViewModel
import com.example.myplaylistmaker.ui.search.view_model_for_activity.screen_state.SearchScreenState
import java.io.IOException

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    // viewModel:
    private val searchViewModel by viewModels<SearchViewModel> { SearchViewModel.getViewModelFactory() }
    private var isClickAllowed = true

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var historyRecycler: RecyclerView
    private lateinit var historyList: List<Track>
    private lateinit var recyclerView: RecyclerView

    private val handler = Handler(Looper.getMainLooper())


    private var isEnterPressed: Boolean = false

    private val KEY_TEXT = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        historyRecycler = binding.historyRecycler

        //делаем ViewModel
        searchViewModel.getStateLiveData().observe(this) { stateLiveData ->

            when (val state = stateLiveData) {
                is SearchScreenState.DefaultSearch -> defaultSearch()
                is SearchScreenState.ConnectionError -> connectionError()
                is SearchScreenState.Loading -> loading()
                is SearchScreenState.NothingFound -> nothingFound()
                is SearchScreenState.SearchIsOk -> searchIsOk(state.data)
                is SearchScreenState.SearchWithHistory -> searchWithHistory(state.historyData)
                else -> {}
            }
        }

        //кнопка назад
        binding.arrowBack3.setOnClickListener {
            finish()
        }

        // ввод строки поиска и обработка кнопок
        onEditorFocus()
        onSearchTextChange()
        onClearIconClick()
        clearIconVisibilityChanger()
        startSearchByEnterPress()

        //поиск
        trackAdapter = TrackAdapter() {
            if (clickDebounce()) {
                clickAdapting(it)
            }
        }

        recyclerView = binding.rvTracks
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = trackAdapter

        //история

        historyAdapter = TrackAdapter() {
            if (clickDebounce()) {
                clickAdapting(it)
            }
        }

        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = historyAdapter
        binding.clearHistoryButton.setOnClickListener {
            historyInVisible()
            searchViewModel.clearHistory()
        }
        historyList = try {
            val historyValue = searchViewModel.provideHistory().value
            historyValue ?: emptyList()

        } catch (e: IOException) {
            emptyList()
        }
        Log.d("historyListActivity", historyList.toString())
    }

    //сохраняем текст при повороте экрана
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        outState.putString(KEY_TEXT, inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString(KEY_TEXT, "")
    }

    //включаем кликдебаунсер
    override fun onResume() {
        super.onResume()
        isClickAllowed = true
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun clickAdapting(item: Track) {
        searchViewModel.addItem(item)
        Log.d("История", "Клик по треку!")

        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra("track", item)
        this.startActivity(intent)
    }

    //видимость кнопки удаления введенной строки (крестик)
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            GONE
        } else {
            VISIBLE
        }
    }

    //поиск
    private fun search() {
        searchViewModel.searchRequesting(binding.inputEditText.text.toString())
    }


    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
    }

    private val searchRunnable = Runnable {
        search()
        // trackAdapter.notifyDataSetChanged()
    }

    //если фокус на поле ввода поиска
    private fun onEditorFocus() {
        binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.inputEditText.text.isEmpty() && searchViewModel.provideHistory().value?.isNotEmpty() ?: false) {
                searchViewModel.clearTrackList()
            } else {
                historyInVisible()
            }
        }
    }

    //поиски
    var searchText = ""

    // когда меняется текст в поисковой строке
    private fun onSearchTextChange() {
        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.inputEditText.hasFocus() && p0?.isEmpty() == true && historyList.isNotEmpty()) {
                    searchViewModel.clearTrackList()
                } else {
                    historyInVisible()
                }
                if (!binding.inputEditText.text.isNullOrEmpty()) {
                    searchText = binding.inputEditText.text.toString()
                    searchDebounce()

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    //поиск по нажатию энтер на клавиатуре
    @SuppressLint("NotifyDataSetChanged")
    private fun startSearchByEnterPress() {
        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.inputEditText.text.isNotEmpty()) {
                    searchText = binding.inputEditText.text.toString()
                    search()
                    trackAdapter.notifyDataSetChanged()
                    isEnterPressed = true
                    handler.postDelayed({ isEnterPressed = false }, 3000L)
                }
                true
            }
            false
        }
    }

    //очистить строку ввода
    private fun onClearIconClick() {
        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(
                binding.inputEditText.windowToken,
                0
            ) // скрыть клавиатуру
            binding.inputEditText.clearFocus()
            searchViewModel.clearTrackList()
        }
    }

    //включает видимость кнопки очистки строки ввода, когда есть какой-либо текст
    private fun clearIconVisibilityChanger() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    //состояния экранов
    private fun defaultSearch() {
        historyInVisible()
        recyclerView.visibility = GONE
        binding.SearchError.visibility = GONE
        binding.SearchErrorText.visibility = GONE
        binding.LineError.visibility = GONE
        binding.LineErrorText.visibility = GONE
        binding.updateButton.visibility = GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loading() {
        binding.progressBar.visibility = VISIBLE
        historyInVisible()
        recyclerView.visibility = GONE
        binding.SearchError.visibility = GONE
        binding.SearchErrorText.visibility = GONE
        binding.LineError.visibility = GONE
        binding.LineErrorText.visibility = GONE
        binding.updateButton.visibility = GONE
        trackAdapter.notifyDataSetChanged()

    }

    private fun searchIsOk(data: List<Track>) {
        binding.progressBar.visibility = GONE
        recyclerView.visibility = VISIBLE
        binding.SearchError.visibility = GONE
        binding.SearchErrorText.visibility = GONE
        binding.LineError.visibility = GONE
        binding.LineErrorText.visibility = GONE
        binding.updateButton.visibility = GONE
        trackAdapter.setItems(data)
        binding.clearHistoryButton.visibility - GONE
        historyInVisible()
    }

    private fun nothingFound() {
        binding.historyText.visibility = GONE
        historyRecycler.visibility = GONE
        binding.clearHistoryButton.visibility = VISIBLE
        recyclerView.visibility = GONE
        binding.SearchError.visibility = VISIBLE
        binding.SearchErrorText.visibility = VISIBLE
        binding.LineError.visibility = GONE
        binding.LineErrorText.visibility = GONE
        binding.updateButton.visibility = GONE
        historyInVisible()
    }

    private fun connectionError() {
        binding.SearchError.visibility = VISIBLE
        binding.SearchErrorText.visibility = VISIBLE
        binding.updateButton.visibility = VISIBLE
        recyclerView.visibility = GONE
        binding.updateButton.setOnClickListener { search() }
        binding.progressBar.visibility = GONE
        historyInVisible()
    }

    private fun searchWithHistory(historyData: List<Track>) {
        Log.d("historyListprovide", historyData.toString())
        historyAdapter.setItems(historyData)
        historyAdapter.notifyDataSetChanged()
        binding.rvTracks.visibility=GONE
        binding.historyText.visibility = VISIBLE
        binding.historyRecycler.visibility = VISIBLE
        binding.clearHistoryButton.visibility = VISIBLE
        recyclerView.visibility = GONE
        binding.SearchError.visibility = GONE
        binding.SearchErrorText.visibility = GONE
        binding.updateButton.visibility = GONE
        binding.LineError.visibility = GONE
        binding.LineErrorText.visibility = GONE
        binding.progressBar.visibility = GONE
    }

    private fun historyInVisible() {
        binding.historyText.visibility = GONE
        historyRecycler.visibility = GONE
        binding.clearHistoryButton.visibility = GONE
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}