package com.example.nyanshop.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.nyanshop.MainActivity
import com.example.nyanshop.databinding.FragmentItemBinding
import com.example.nyanshop.databinding.ItemCardBinding
import com.example.nyanshop.model.Item

class ItemAdapter(private val context: Context, private val itemList: List<Item>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(val context: Context, val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            context,
            ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.binding.tvItemName.text = item.name
        holder.binding.tvItemDescription.text = item.description
        holder.binding.cvItemCard.setOnClickListener {
            val intent = Intent(holder.context, MainActivity::class.java)
            holder.context.startActivity(intent)
        }
    }
}