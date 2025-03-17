package com.example.nyanshop.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nyanshop.fragment.ItemFragment
import com.example.nyanshop.fragment.ReviewFragment
import com.example.nyanshop.fragments.HomeFragment
import com.example.nyanshop.fragments.PetFragment
import com.example.nyanshop.fragments.ProfileFragment

class TabHomeVPAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HomeFragment()
            1 -> PetFragment()
            2 -> ProfileFragment()
            else -> HomeFragment()
        }
    }

}