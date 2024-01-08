package com.example.myplaylistmaker.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myplaylistmaker.databinding.FragmentTestBinding

class TestFragment : Fragment() {

    private var _binding: FragmentTestBinding? = null
    private val binding: FragmentTestBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button1.setOnClickListener {
            val x = parseStringToInt(binding.textInput.text.toString())
            binding.outPut.text = x.toString()
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun singleNumber(array: IntArray): Int {

        for (i in array) {

            if (decomposition(number = i, array = array) != true) {
                return i
            }
        }
        return array[0]
    }


    private fun decomposition(array: IntArray, number: Int): Boolean {

        // массив [1, 23, 35, 1, 12, 4, 23, 4, 35], 4
        var countOfElements = 0

        for (i in array) {

            if (i == number) {
                countOfElements++
            }

        }

        if (countOfElements == 2) return true
        return false

    }

    private fun parseStringToInt(text: String): Int? {

        if (text.length == 0) return null
        var num = 0
        var allDigits = true
        for (c in text) {
            if (c in '0'..'9') {
                num = num * 10 + (c - '0')
            } else if (c in 'a'..'z') {
                return null
            } else if (c =='-') {
                return null
            } else allDigits = false
        }
        return if (allDigits) num else null
    }


    private fun numberToSquare(number: Int): Int {

        return number * number

    }

    private fun parseCharToInt(char: Char): Int {

        return char.code
    }

}