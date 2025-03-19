package com.example.nyanshop.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nyanshop.MapActivity
import com.example.nyanshop.database.DatabaseHelper
import com.example.nyanshop.databinding.PetCardBinding
import com.example.nyanshop.model.Pet
import com.example.nyanshop.model.Store

class PetAdapter(private val context: Context, private val petList: List<Pet>) :
    RecyclerView.Adapter<PetAdapter.ViewHolder>() {

    private val db = DatabaseHelper(context)

    class ViewHolder(val binding: PetCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            PetCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return petList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pet = petList[position]
        holder.binding.tvPetName.text = pet.petName
        holder.binding.tvPetType.text = pet.petType
        holder.binding.tvPetPrice.text = "$${pet.petPrice}"

        val store: Store? = db.getStore(pet.storeId)

        if (store == null) {
            Log.e("STORE_ERROR", "Store ID ${pet.storeId} not found in database")
        } else {
            Log.d("STORE_INFO", "Found Store: ${store.storeName} for Store ID: ${store.storeId}")
        }

        holder.binding.btnBuyPet.setOnClickListener {
            val intent = Intent(context, MapActivity::class.java).apply {
                putExtra("PET_ID", pet.petId)
                putExtra("STORE_ID", pet.storeId)
                putExtra("ITEM_NAME", pet.petName)
                putExtra("ITEM_PRICE", pet.petPrice.toString())
                putExtra("STORE_NAME", store?.storeName ?: "Nyanshop Central")
            }
            context.startActivity(intent)
        }
    }

}
