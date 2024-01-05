package com.example.myplaylistmaker.ui.mediaLibrary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myplaylistmaker.R
import com.example.myplaylistmaker.databinding.FragmentMediaLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator


class MediaLibraryFragment : Fragment() {

    private  var _binding: FragmentMediaLibraryBinding? = null
    private val binding get() = _binding!!
    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreate(savedInstanceState)

        _binding = FragmentMediaLibraryBinding.inflate(layoutInflater)
        binding.pager.adapter = FragmentAdapter(this)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favoritTracks)
                1 -> tab.text = getString(R.string.playlist)
            }
        }
        tabMediator.attach()

        return binding.root
    }

    override fun onDestroyView() {

        tabMediator.detach()
        super.onDestroyView()
    }
}

