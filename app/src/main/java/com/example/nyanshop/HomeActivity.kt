package com.example.nyanshop

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.nyanshop.adapter.TabHomeVPAdapter
import com.example.nyanshop.database.DatabaseHelper
import com.example.nyanshop.model.Pet
import com.example.nyanshop.model.Store
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
            val updatedName = sharedPref.getString("username", user?.name)
            tvHello.text = updatedName ?: "Guest"

            with(sharedPref.edit()) {
                putString("email", user?.email)
                putString("username", updatedName)
                putInt("item_id", user?.item_id ?: -1)
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

        insertPetsIfNotExists()
        insertStoresIfNotExists()
    }

    fun switchToShopTab() {
        viewPager.currentItem = 1
    }

    private fun insertPetsIfNotExists() {
        val testData = listOf(
            Pet(1, "Siamoy", "Siamese", 2, 500, "ST001"),
            Pet(2, "Percy", "Persian", 3, 300, "ST002"),
            Pet(3, "MainKun", "Maine Coon", 4, 400, "ST002"),
            Pet(4, "Begal", "Bengal", 1, 600, "ST001"),
            Pet(5, "Sphyky", "Sphynx", 5, 700, "ST001")
        )

        for (pet in testData) {
            if (db.getPetById(pet.petId) == null) {
                val isInserted = db.insertPet(pet)
                if (isInserted) {
                    Log.i("DB_INSERT", "Pet inserted: ${pet.petName}")
                } else {
                    Log.e("DB_ERROR", "Failed to insert pet: ${pet.petName}")
                }
            } else {
                Log.i("DB_EXISTS", "Pet already exists: ${pet.petName}")
            }
        }
    }

    private fun insertStoresIfNotExists() {
        val storeData = listOf(
            Store("ST001", "Nyanshop Central", "Jakarta", -6.20201, 106.78113),
            Store("ST002", "Nyanshop West", "Bandung", -6.91746, 107.61912)
        )

        for (store in storeData) {
            if (db.getStore(store.storeId) == null) {
                val isInserted = db.insertStore(store)
                if (isInserted) {
                    Log.i("DB_INSERT", "Store inserted: ${store.storeName}")
                } else {
                    Log.e("DB_ERROR", "Failed to insert store: ${store.storeName}")
                }
            } else {
                Log.i("DB_EXISTS", "Store already exists: ${store.storeName}")
            }
        }
    }
}
