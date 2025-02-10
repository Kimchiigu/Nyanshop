package com.example.nyanshop.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nyanshop.R
import com.example.nyanshop.adapter.ItemAdapter
import com.example.nyanshop.database.DatabaseHelper
import com.example.nyanshop.databinding.FragmentItemBinding
import com.example.nyanshop.model.Item

class ItemFragment : Fragment() {

    private lateinit var binding: FragmentItemBinding
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemBinding.inflate(layoutInflater, container, false)

        databaseHelper = DatabaseHelper(requireContext())
        setUpRecycler()

        // belum ada validasi
        binding.btnAdd.setOnClickListener {
            val name = binding.etItemName.text.toString()
            val description = binding.etItemDescription.text.toString()

            databaseHelper.insertItem(Item(0, name, description))
            resetUI()
            setUpRecycler()
        }

        binding.btnUpdate.setOnClickListener {
            val id = binding.etItemId.text.toString()
            val name = binding.etItemName.text.toString()
            val description = binding.etItemDescription.text.toString()

            databaseHelper.updateItem(Item(id.toInt(), name, description))
            resetUI()
            setUpRecycler()
        }

        binding.btnDelete.setOnClickListener {
            val id = binding.etItemId.text.toString()

            databaseHelper.deleteItem(id)
            resetUI()
            setUpRecycler()
        }

        return binding.root
    }

    private fun resetUI() {
        binding.etItemId.text.clear()
        binding.etItemName.text.clear()
        binding.etItemDescription.text.clear()
    }

    private fun setUpRecycler() {
        val items = databaseHelper.getItems()
        itemAdapter = ItemAdapter(requireContext(), items)
        binding.rvItemList.adapter = itemAdapter
        binding.rvItemList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}