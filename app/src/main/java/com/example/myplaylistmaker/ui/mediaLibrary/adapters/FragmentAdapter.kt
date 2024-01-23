package com.example.myplaylistmaker.ui.mediaLibrary.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myplaylistmaker.ui.mediaLibrary.fragments.FavoritesFragment
import com.example.myplaylistmaker.ui.mediaLibrary.fragments.PlaylistFragment

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