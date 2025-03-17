package com.example.nyanshop

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nyanshop.database.DatabaseHelper
import com.example.nyanshop.databinding.ActivityRegisterBinding
import com.example.nyanshop.model.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)

        binding.btnRegister.setOnClickListener {
            registerUser()
        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun registerUser() {
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val isMale = binding.rbMale.isChecked
        val isFemale = binding.rbFemale.isChecked
        val isAdmin = binding.swAdmin.isChecked
        val isTncChecked = binding.cbTnc.isChecked

        if (username.isEmpty()) {
            showToast("Username can't be empty!")
            return
        }
        if (username.length <= 5) {
            showToast("Username must be more than 5 characters")
            return
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Please enter a valid email!")
            return
        }
        if (password.isEmpty()) {
            showToast("Password can't be empty!")
            return
        }
        if (password.length <= 5) {
            showToast("Password must be more than 5 characters")
            return
        }
        if (!isMale && !isFemale) {
            showToast("Please select a gender!")
            return
        }
        if (!isTncChecked) {
            showToast("You must agree to the Terms and Conditions!")
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Are you sure you want to proceed with these details?")
            .setPositiveButton("Yes") { _, _ ->
                val gender = if (isMale) "Male" else "Female"
                val newUser = User(0, username, email, password, gender, isAdmin, null)

                val isInserted = db.insertUser(newUser)
                if (isInserted) {
                    showToast("Registration successful!")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showToast("Registration failed. Try again!")
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}