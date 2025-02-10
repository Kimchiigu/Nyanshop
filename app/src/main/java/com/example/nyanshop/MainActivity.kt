package com.example.nyanshop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // lateinit ini variabelnya akan dinisialisasi nanti (karena kan kita nunggu inputan user)
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // R ini adalah Resources, bisa akses folder/file di directory kita
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        tvRegister = findViewById(R.id.tv_register)

        btnLogin.setOnClickListener {
            val username = etUsername.text
            val password = etPassword.text

            if (username.isEmpty() || password.isEmpty()) {
                // context = mau taruh mana toast nya
                //           kalau this berarti taruh disini (MainACtivity.kt)
                // char sequence = isi toast
                // duration = berapa lama mau ditampilin
                // return nya itu type functionnya
                Toast.makeText(this, "Username or Password can't be empty!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
            Log.i("LOGIN_USERNAME", username.toString())
            Log.i("LOGIN_PASSWORD", password.toString())
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("username", username.toString())
            startActivity(intent)
        }

        tvRegister.setOnClickListener {
            // Intent = ketika mau pindah halaman
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

}