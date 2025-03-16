package com.example.nyanshop

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nyanshop.databinding.ActivitySmsBinding
import com.example.nyanshop.service.BackgroundService

class SmsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySmsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySmsBinding.inflate(layoutInflater)

        setContentView(binding.root)

//        kalau belum ada permission, bkl munculin permintaan
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
//            request code asal, tp dia kek id, jadi harus beda antar request
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECEIVE_SMS), 100)
        }

        binding.btnSend.setOnClickListener {
            val phoneNumber = binding.etPhoneNumber.text.toString()
            val message = binding.etMessage.text.toString()

            sendSMS(phoneNumber, message)
        }

        binding.btnStartService.setOnClickListener {
            val intent = Intent(this, BackgroundService::class.java)
//            start service
            startService(intent)
        }
    }

    private fun sendSMS(phoneNumber: String, message: String) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), 101)
        } else {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        }
    }
}