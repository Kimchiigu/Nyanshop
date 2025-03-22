package com.example.nyanshop.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nyanshop.R
import com.example.nyanshop.database.DatabaseHelper
import com.example.nyanshop.model.User
import com.example.nyanshop.MainActivity
import android.app.AlertDialog

class ProfileFragment : Fragment() {
    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileEmail: TextView
    private lateinit var btnLogout: Button
    private lateinit var db: DatabaseHelper
    private var currentUser: User? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        tvProfileName = view.findViewById(R.id.tv_profile_name)
        tvProfileEmail = view.findViewById(R.id.tv_profile_email)
        btnLogout = view.findViewById(R.id.btn_logout)

        db = DatabaseHelper(requireContext())

        // Get current user from shared preferences
        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val email = sharedPref.getString("email", null)

        email?.let {
            currentUser = db.getUserByEmail(it)
            tvProfileName.text = currentUser?.name ?: "User"
            tvProfileEmail.text = currentUser?.email ?: "user@example.com"
        }

        // Open the edit profile modal when the user clicks the "Edit Profile" button
        view.findViewById<View>(R.id.layout_edit_profile).setOnClickListener {
            showEditProfileDialog()
        }

        btnLogout.setOnClickListener {
            logoutUser()
        }

        return view
    }

    private fun showEditProfileDialog() {
        // Inflate the dialog view
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.profile_edit_dialog, null)
        val etName = dialogView.findViewById<EditText>(R.id.et_edit_name)
        val etPassword = dialogView.findViewById<EditText>(R.id.et_edit_password)
        val btnSave = dialogView.findViewById<Button>(R.id.btn_save_changes)
        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)

        // Set current user details in the dialog
        etName.setText(currentUser?.name)
        etPassword.setText(currentUser?.password)

        // Set up the alert dialog
        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        // Handle Save button click
        btnSave.setOnClickListener {
            val newName = etName.text.toString()
            val newPassword = etPassword.text.toString()

            // Ensure that both fields are filled out
            if (newName.isNotEmpty() && newPassword.isNotEmpty()) {
                // Update the user's name and password in the database
                currentUser?.apply {
                    name = newName
                    password = newPassword
                }

                // Update the database
                db.updateUser(currentUser!!)

                // Update the UI with the new details
                tvProfileName.text = newName
                tvProfileEmail.text = currentUser?.email

                Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()

                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Both fields are required!", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle Cancel button click
        btnCancel.setOnClickListener {
            dialog.dismiss()  // Simply dismiss the dialog
        }

        // Show the dialog
        dialog.show()
    }

    private fun logoutUser() {
        // Clear the user's session and log out
        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        Toast.makeText(requireContext(), "Logged out successfully!", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
