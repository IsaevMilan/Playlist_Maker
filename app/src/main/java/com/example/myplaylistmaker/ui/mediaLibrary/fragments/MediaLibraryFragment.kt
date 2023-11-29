package com.example.myplaylistmaker.ui.mediaLibrary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.example.myplaylistmaker.databinding.FragmentMediaLibraryBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import android.view.View
import android.view.ViewGroup


class MediaLibraryFragment : Fragment(), SelectPage {

    private lateinit var binding: FragmentMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator
     override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        binding= FragmentMediaLibraryBinding.inflate(layoutInflater)

        binding.pager.adapter = FragmentAdapter(this)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> tab.text = "Избранные треки"
                1 -> tab.text = "Плейлист"
            }
        }
        tabMediator.attach()

        binding.tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    val currentPosition = tab.position
                    binding.pager.currentItem = currentPosition
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            }

        )
        return binding.root
    }


    override fun NavigateTo(page: Int) {
        binding.pager.currentItem = page
    }

     override fun onDestroyView() {
    super.onDestroyView()
    tabMediator.detach()
    }
 }

