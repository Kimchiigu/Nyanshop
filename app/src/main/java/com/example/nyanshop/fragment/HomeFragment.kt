package com.example.nyanshop.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nyanshop.HomeActivity
import com.example.nyanshop.R

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val btnShopNow = view.findViewById<Button>(R.id.btn_shop_now)
        btnShopNow.setOnClickListener { v: View? ->
            (requireActivity() as? HomeActivity)?.switchToShopTab()
        }

        return view
    }
}