package com.example.nyanshop.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.nyanshop.MapActivity
import com.example.nyanshop.adapter.PetAdapter
import com.example.nyanshop.database.DatabaseHelper
import com.example.nyanshop.databinding.FragmentPetBinding
import com.example.nyanshop.model.Pet
import org.json.JSONException

class PetFragment : Fragment() {

    private lateinit var binding: FragmentPetBinding
    private lateinit var petAdapter: PetAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var requestQueue: RequestQueue

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPetBinding.inflate(inflater, container, false)
        val view = binding.root

        databaseHelper = DatabaseHelper(requireContext())
        requestQueue = Volley.newRequestQueue(requireContext())

        fetchData()

        return view
    }

    private fun fetchData() {
        Log.i("TEST_MODE", "Using hardcoded test data instead of API")

        val testData = listOf(
            Pet(0, "Siamoy", "Siamese", 2, 500, "ST001"),
            Pet(1, "Percy", "Persian", 3, 300, "ST002"),
            Pet(2, "MainKun", "Maine Coon", 4, 400, "ST002"),
            Pet(3, "Begal", "Bengal", 1, 600, "ST001"),
            Pet(4, "Sphyky", "Sphynx", 5, 700, "ST001")
        )

        Log.i("TEST_DATA", "Hardcoded Pet List: $testData")
        setUpRecycler(testData)
    }

    private fun setUpRecycler(pets: List<Pet>) {
        petAdapter = PetAdapter(requireContext(), pets)
        binding.rvPetList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = petAdapter
        }
    }
}
