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
import com.example.nyanshop.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text
            val password = binding.etPassword.text
            val rbMale = binding.rbMale.isChecked
            val rbFemale = binding.rbFemale.isChecked
            val switchAdmin = binding.swAdmin.isChecked
            val cbTnc = binding.cbTnc.isChecked

            if (username.isEmpty()) {
                Toast.makeText(this, "Username can't be empty!", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Password can't be empty!", Toast.LENGTH_SHORT).show()
            } else if (!rbMale && !rbFemale) {
                Toast.makeText(this, "Gender can't be empty!", Toast.LENGTH_SHORT).show()
            } else if (!switchAdmin) {
                Toast.makeText(this, "Admin switch must be toggled!", Toast.LENGTH_SHORT).show()
            } else if (!cbTnc) {
                Toast.makeText(this, "TnC must be agreed!", Toast.LENGTH_SHORT).show()
            } else {
                val builder = AlertDialog.Builder(this)

                builder.setTitle("Confirmation")
                builder.setMessage("Are you sure with your information?")

                builder.setPositiveButton("Yes") { dialog, which ->
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

                builder.setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()
            }
        }

        binding.tvLogin.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}