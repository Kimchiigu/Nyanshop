package com.example.nyanshop.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nyanshop.fragment.ItemFragment
import com.example.nyanshop.fragment.ReviewFragment

class TabHomeVPAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> ItemFragment()
            1 -> ReviewFragment()
            else -> ItemFragment()
        }
    }

}