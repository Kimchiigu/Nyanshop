package com.example.nyanshop.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import com.example.nyanshop.R
import com.example.nyanshop.adapter.ItemAdapter
import com.example.nyanshop.database.DatabaseHelper
import com.example.nyanshop.databinding.FragmentItemBinding
import com.example.nyanshop.model.Item

class ItemFragment : Fragment() {

    private lateinit var binding: FragmentItemBinding
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var requestQueue: RequestQueue

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemBinding.inflate(layoutInflater, container, false)

        databaseHelper = DatabaseHelper(requireContext())
        requestQueue = Volley.newRequestQueue(context)
        setUpRecycler()

        binding.btnGetData.setOnClickListener {
//            masukin link generated dari npoint.io yang kita simpan jsonnya
            val url = "https://api.npoint.io/fdc3e8641845a1a6bef6"
            val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
                try {
                    databaseHelper.deleteAllItem()
                    val itemArray = response.getJSONArray("items")
                    for (i in 0 until itemArray.length()) {
                        val itemObject = itemArray.getJSONObject(i)
                        val item = Item(
                            0,
                            itemObject.getString("name"),
                            itemObject.getString("description")
                        )
                        databaseHelper.insertItem(item)
                    }
                    setUpRecycler()
                } catch (e: JSONException) {
                    Toast.makeText(context, "Get data from url error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },{ error ->
                Log.e("Volley error", error.toString())
            })
            requestQueue.add(request)
        }

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