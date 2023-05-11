package com.example.myplaylistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputEditText

class SearchActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    companion object {
        private const val INPUT_TEXT = "input_text"
    }

    private var saveText=""

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TEXT,saveText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
         saveText= savedInstanceState.getString(INPUT_TEXT,"")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val backButt = findViewById<ImageView>(R.id.arrowBack3)

        backButt.setOnClickListener { onBackPressed()
        }




        clearButton.setOnClickListener {
            inputEditText.setText("")
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(inputEditText.windowToken, 0) // скрыть клавиатуру
            inputEditText.clearFocus()
        }


    val simpleTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.isNullOrEmpty()) {
                clearButton.visibility = View.GONE
            } else {
                clearButton.visibility = View.VISIBLE
            }

        }

        override fun afterTextChanged(s: Editable?) {

        }
    }
    inputEditText.addTextChangedListener(simpleTextWatcher)



 }







}