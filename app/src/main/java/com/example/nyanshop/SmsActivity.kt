package com.example.nyanshop

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nyanshop.adapter.MessageAdapter
import com.example.nyanshop.databinding.ActivitySmsBinding
import com.example.nyanshop.model.Message
import com.example.nyanshop.service.BackgroundService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SmsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySmsBinding
    private lateinit var messageAdapter: MessageAdapter
    private val messages = ArrayList<Message>()
    private val customerServiceNumber = "+1234567890"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySmsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etPhoneNumber.setText(customerServiceNumber)

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECEIVE_SMS), 100)
        }

        setupRecyclerView()
        addWelcomeMessage()

        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
                binding.etMessage.text.clear()
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        val intent = Intent(this, BackgroundService::class.java)
        startService(intent)
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(messages)
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(this@SmsActivity).apply {
                stackFromEnd = true
            }
            adapter = messageAdapter
        }
    }

    private fun addWelcomeMessage() {
        val welcomeMessage = Message(
            "Hello! Welcome to Nyanshop Customer Service. How can I help you today?",
            getCurrentTime(),
            false // Received message (from customer service)
        )
        messages.add(welcomeMessage)
        messageAdapter.notifyItemInserted(messages.size - 1)
        binding.rvMessages.scrollToPosition(messages.size - 1)
    }

    private fun sendMessage(messageText: String) {
        val sentMessage = Message(
            messageText,
            getCurrentTime(),
            true
        )
        messages.add(sentMessage)
        messageAdapter.notifyItemInserted(messages.size - 1)
        binding.rvMessages.scrollToPosition(messages.size - 1)

        val phoneNumber = binding.etPhoneNumber.text.toString()
        sendSMS(phoneNumber, messageText)

        simulateResponse(messageText)
    }

    private fun simulateResponse(userMessage: String) {
        binding.rvMessages.postDelayed({
            val responseText = when {
                userMessage.contains("hello", ignoreCase = true) ||
                        userMessage.contains("hi", ignoreCase = true) ->
                    "Hello there! How can I assist you with your pet needs today?"

                userMessage.contains("cat", ignoreCase = true) ->
                    "We have several cats available including Persian, Siamese, and Maine Coon breeds. Would you like more information about any specific breed?"

                userMessage.contains("dog", ignoreCase = true) ->
                    "We have many dog breeds available including Golden Retrievers, Labradors, and Poodles. Is there a specific breed you're interested in?"

                userMessage.contains("price", ignoreCase = true) ||
                        userMessage.contains("cost", ignoreCase = true) ->
                    "Our prices vary depending on the pet breed and age. Cats range from $80-$200, while dogs range from $150-$500. Premium breeds may cost more."

                userMessage.contains("location", ignoreCase = true) ||
                        userMessage.contains("address", ignoreCase = true) ->
                    "Our main store is located at BINUS University, Jakarta. We also have a branch in Central Jakarta. Would you like the exact address?"

                userMessage.contains("thank", ignoreCase = true) ->
                    "You're welcome! Feel free to reach out if you have any other questions. Have a great day!"

                else -> "Thank you for your message. Our team will get back to you shortly. Is there anything specific you'd like to know about our pets?"
            }

            val receivedMessage = Message(
                responseText,
                getCurrentTime(),
                false
            )
            messages.add(receivedMessage)
            messageAdapter.notifyItemInserted(messages.size - 1)
            binding.rvMessages.scrollToPosition(messages.size - 1)
        }, 1000)
    }

    private fun sendSMS(phoneNumber: String, message: String) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), 101)
        } else {
            try {
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            } catch (e: Exception) {
                Toast.makeText(this, "Failed to send SMS: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        return sdf.format(Date())
    }
}

