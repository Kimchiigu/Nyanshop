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
        val url = "https://api.npoint.io/54d1f95bddbcb0670225"
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                databaseHelper.deleteAllPets()
                val petArray = response.getJSONArray("pets")
                val petsList = mutableListOf<Pet>()

                for (i in 0 until petArray.length()) {
                    val petObject = petArray.getJSONObject(i)
                    val pet = Pet(
                        0,
                        petObject.getString("name"),
                        petObject.getString("type"),
                        petObject.getInt("age"),
                        petObject.getInt("price"),
                        petObject.getString("storeId")
                    )
                    databaseHelper.insertPet(pet)
                    petsList.add(pet)
                }

                setUpRecycler(petsList)

            } catch (e: JSONException) {
                Toast.makeText(context, "Error parsing data: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("JSONError", "Error: ${e.message}")
            }
        }, { error ->
            Log.e("VolleyError", "Error fetching data: ${error.message}")
            Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
        })

        requestQueue.add(request)
    }

    private fun setUpRecycler(pets: List<Pet>) {
        petAdapter = PetAdapter(requireContext(), pets)
        binding.rvPetList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = petAdapter
        }
    }
}
