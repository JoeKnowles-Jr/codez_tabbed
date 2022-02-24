package com.jk.codez.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CodezPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private var currentFragment: Fragment? = null

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                currentFragment = LocalFragment()
            }
            1 -> {
                currentFragment = RemoteFragment()
            }
        }
        return currentFragment ?: LocalFragment()
    }

    override fun getItemCount(): Int {
        // Show 2 total pages.
        return 2
    }
}