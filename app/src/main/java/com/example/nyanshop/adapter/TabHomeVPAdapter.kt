package com.example.nyanshop.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nyanshop.fragments.HomeFragment
import com.example.nyanshop.fragments.PetFragment
import com.example.nyanshop.fragments.ProfileFragment
import com.example.nyanshop.model.User

class TabHomeVPAdapter(fa: FragmentActivity, private val user: User?) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> PetFragment()
            2 -> {
                val fragment = ProfileFragment()
                val bundle = Bundle().apply {
                    putString("username", user?.name ?: "Guest")
                    putString("email", user?.email ?: "N/A")
                }
                fragment.arguments = bundle
                fragment
            }
            else -> HomeFragment()
        }
    }
}
