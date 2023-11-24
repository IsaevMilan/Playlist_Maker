package com.example.myplaylistmaker.ui.mediaLibrary.media_library_activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myplaylistmaker.databinding.ActivityMediaLibraryBinding
import com.example.myplaylistmaker.ui.mediaLibrary.fragments.FragmentAdapter
import com.example.myplaylistmaker.ui.mediaLibrary.fragments.SelectPage
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MediaLibraryActivity : AppCompatActivity(), SelectPage {

    private lateinit var binding: ActivityMediaLibraryBinding

    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        binding.arrowBack2.setOnClickListener {
            finish()
        }
        setContentView(binding.root)

        val adapter = FragmentAdapter(supportFragmentManager, lifecycle)
        binding.pager.adapter = adapter

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

    }


    override fun NavigateTo(page: Int) {
        binding.pager.currentItem = page
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
    }
