package com.example.nyanshop.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nyanshop.MainActivity
import com.example.nyanshop.databinding.PetCardBinding
import com.example.nyanshop.model.Pet

class PetAdapter(private val context: Context, private val petList: List<Pet>) :
    RecyclerView.Adapter<PetAdapter.ViewHolder>() {

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
        holder.binding.tvPetPrice.text = pet.petPrice.toString()

        holder.binding.cvPetCard.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}
