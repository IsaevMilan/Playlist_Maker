package com.example.myplaylistmaker.ui.mediaLibrary.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myplaylistmaker.databinding.FragmentFavoritesBinding
import com.example.myplaylistmaker.ui.mediaLibrary.viewModels.FavoritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    private val favoritesViewModel by viewModel<FavoritesViewModel>()
    private lateinit var nullableFavouritesBinding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("onCreateView", "FavouritesFragment")
        nullableFavouritesBinding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return nullableFavouritesBinding.root
    }

    companion object {
        fun newInstance() = FavoritesFragment()

    }
}