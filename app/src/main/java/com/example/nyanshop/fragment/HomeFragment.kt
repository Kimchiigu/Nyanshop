package com.example.nyanshop.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.nyanshop.HomeActivity
import com.example.nyanshop.database.DatabaseHelper
import com.example.nyanshop.databinding.FragmentHomeBinding
import com.example.nyanshop.model.Pet

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var pet: Pet? = null
    private lateinit var db: DatabaseHelper
    private lateinit var petList: List<Pet>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        db = DatabaseHelper(requireContext())

        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val userName = sharedPref.getString("username", "Guest")
        val itemId = sharedPref.getInt("item_id", -1)

        Log.i("USER_INFO", "User's ItemID: $itemId")
        Log.i("USER_INFO", "Current User: $userName")
        pet = db.getPetById(itemId)
        petList = db.getPets()
        Log.i("PET_INFO", "Pet List: $petList")

        Log.i("PET_INFO", "Pet Data : $pet")

        if (itemId == 0) {
            binding.layoutNoPet.visibility = View.VISIBLE
            binding.layoutPetInfo.visibility = View.GONE
        } else {
            binding.layoutNoPet.visibility = View.GONE
            binding.layoutPetInfo.visibility = View.VISIBLE

            binding.tvPetName.text = pet?.petName ?: "Unknown Pet"
            binding.tvPetDesc.text = "Your adorable companion!"
        }

        binding.btnShopNow.setOnClickListener {
            (requireActivity() as? HomeActivity)?.switchToShopTab()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
