package com.example.nyanshop

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.nyanshop.adapter.TabHomeVPAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var tvHello: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)
        tvHello = findViewById(R.id.tv_hello)

        val username = intent.getStringExtra("username") ?: "User"
        tvHello.text = "Hello, $username"

        val adapter = TabHomeVPAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Home"
                1 -> "Shop"
                2 -> "Profile"
                else -> "Home"
            }
        }.attach()
    }

    fun switchToShopTab() {
        viewPager.currentItem = 1
    }
}
