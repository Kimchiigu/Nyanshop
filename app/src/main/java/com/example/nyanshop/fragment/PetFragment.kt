package com.example.nyanshop.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nyanshop.adapter.PetAdapter
import com.example.nyanshop.database.DatabaseHelper
import com.example.nyanshop.databinding.FragmentPetBinding
import com.example.nyanshop.model.Pet

class PetFragment : Fragment() {

    private lateinit var binding: FragmentPetBinding
    private lateinit var petAdapter: PetAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPetBinding.inflate(inflater, container, false)
        val view = binding.root

        databaseHelper = DatabaseHelper(requireContext())

        fetchData()
        return view
    }

    private fun fetchData() {
        val petsFromDb = databaseHelper.getPets()
        Log.i("DB_FETCH", "Fetched pets: $petsFromDb")

        setUpRecycler(petsFromDb)
    }

    private fun setUpRecycler(pets: List<Pet>) {
        petAdapter = PetAdapter(requireContext(), pets)
        binding.rvPetList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = petAdapter
        }
    }
}
