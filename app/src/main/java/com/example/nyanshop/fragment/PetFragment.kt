package com.example.nyanshop.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.example.nyanshop.MapActivity
import com.example.nyanshop.R
import com.example.nyanshop.adapter.ItemAdapter
import com.example.nyanshop.adapter.PetAdapter
import com.example.nyanshop.database.DatabaseHelper
import com.example.nyanshop.databinding.FragmentItemBinding
import com.example.nyanshop.databinding.FragmentPetBinding

class PetFragment : Fragment() {

    private lateinit var binding: FragmentPetBinding
    private lateinit var petAdapter: PetAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var requestQueue: RequestQueue

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pet, container, false)

        // Set up click listeners for buy buttons
        setupBuyButton(
            view,
            R.id.btn_buy_1,
            R.id.tv_pet_name_1,
            R.id.tv_pet_price_1,
            1
        )

        setupBuyButton(
            view,
            R.id.btn_buy_2,
            R.id.tv_pet_name_2,
            R.id.tv_pet_price_2,
            2
        )

        return view
    }

    private fun setupBuyButton(view: View, buttonId: Int, nameId: Int, priceId: Int, itemId: Int) {
        val buyButton = view.findViewById<Button>(buttonId)
        val petName = view.findViewById<TextView>(nameId).text.toString()
        val petPrice = view.findViewById<TextView>(priceId).text.toString()

        buyButton.setOnClickListener {
            // Navigate to MapActivity with pet details
            val intent = Intent(activity, MapActivity::class.java)
            intent.putExtra("ITEM_NAME", petName)
            intent.putExtra("ITEM_PRICE", petPrice)
            intent.putExtra("ITEM_ID", itemId)
            startActivity(intent)
        }
    }
}

