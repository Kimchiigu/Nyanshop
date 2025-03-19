package com.example.nyanshop

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.nyanshop.adapter.TabHomeVPAdapter
import com.example.nyanshop.database.DatabaseHelper
import com.example.nyanshop.model.User
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var tvHello: TextView
    private lateinit var db: DatabaseHelper
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)
        tvHello = findViewById(R.id.tv_hello)

        db = DatabaseHelper(this)

        val sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val storedEmail = sharedPref.getString("email", null)

        val email = intent.getStringExtra("email") ?: storedEmail

        if (email != null) {
            user = db.getUserByEmail(email)
            tvHello.text = user?.name ?: "Guest"

            with(sharedPref.edit()) {
                putString("email", user?.email)
                putString("username", user?.name)
                apply()
            }
        } else {
            tvHello.text = "Guest"
        }

        val adapter = TabHomeVPAdapter(this, user)
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
