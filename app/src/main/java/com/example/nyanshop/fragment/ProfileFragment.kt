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
import android.widget.LinearLayout
import com.example.nyanshop.SmsActivity

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

        val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val email = sharedPref.getString("email", null)

        email?.let {
            currentUser = db.getUserByEmail(it)
            tvProfileName.text = currentUser?.name ?: "User"
            tvProfileEmail.text = currentUser?.email ?: "user@example.com"
        }

        view.findViewById<View>(R.id.layout_edit_profile).setOnClickListener {
            showEditProfileDialog()
        }

        view.findViewById<LinearLayout>(R.id.layout_settings).setOnClickListener {
            val intent = Intent(requireContext(), SmsActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            logoutUser()
        }

        return view
    }

    private fun showEditProfileDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.profile_edit_dialog, null)
        val etName = dialogView.findViewById<EditText>(R.id.et_edit_name)
        val etPassword = dialogView.findViewById<EditText>(R.id.et_edit_password)
        val btnSave = dialogView.findViewById<Button>(R.id.btn_save_changes)
        val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)

        etName.setText(currentUser?.name)
        etPassword.setText(currentUser?.password)

        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        btnSave.setOnClickListener {
            val newName = etName.text.toString()
            val newPassword = etPassword.text.toString()

            if (newName.isNotEmpty() && newPassword.isNotEmpty()) {
                currentUser?.apply {
                    name = newName
                    password = newPassword
                }

                db.updateUser(currentUser!!)

                tvProfileName.text = newName
                tvProfileEmail.text = currentUser?.email

                val sharedPref = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                sharedPref.edit().putString("username", newName).apply()

                Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()

                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Both fields are required!", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
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
