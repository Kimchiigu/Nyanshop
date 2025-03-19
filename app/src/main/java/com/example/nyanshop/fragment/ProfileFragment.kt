package com.example.nyanshop.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nyanshop.MainActivity
import com.example.nyanshop.R
import com.example.nyanshop.SmsActivity

class ProfileFragment : Fragment() {
    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileEmail: TextView
    private lateinit var btnLogout: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        tvProfileName = view.findViewById(R.id.tv_profile_name)
        tvProfileEmail = view.findViewById(R.id.tv_profile_email)
        btnLogout = view.findViewById(R.id.btn_logout)

        // Get arguments passed from HomeActivity
        val username = arguments?.getString("username", "User") ?: "User"
        val email = arguments?.getString("email", "user@example.com") ?: "user@example.com"

        tvProfileName.text = username
        tvProfileEmail.text = email

        view.findViewById<View>(R.id.layout_settings).setOnClickListener {
            val intent = Intent(requireContext(), SmsActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            logoutUser()
        }

        return view
    }

    private fun logoutUser() {
        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        Toast.makeText(requireContext(), "Logged out successfully!", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}

