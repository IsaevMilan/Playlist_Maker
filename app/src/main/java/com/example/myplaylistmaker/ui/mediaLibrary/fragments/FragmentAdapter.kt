package com.example.myplaylistmaker.ui.mediaLibrary.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(
    parentFragment: Fragment
) :
    FragmentStateAdapter(parentFragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            FavoritesFragment.newInstance()
        } else {
            PlaylistFragment.newInstance()
        }
    }

}