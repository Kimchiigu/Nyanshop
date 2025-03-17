package com.example.nyanshop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nyanshop.R
import com.example.nyanshop.model.Message

class MessageAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layoutSent = view.findViewById<View>(R.id.layout_sent)
        val layoutReceived = view.findViewById<View>(R.id.layout_received)
        val tvSentMessage = view.findViewById<TextView>(R.id.tv_sent_message)
        val tvSentTime = view.findViewById<TextView>(R.id.tv_sent_time)
        val tvReceivedMessage = view.findViewById<TextView>(R.id.tv_received_message)
        val tvReceivedTime = view.findViewById<TextView>(R.id.tv_received_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        if (message.isSent) {
            // Show sent message, hide received message
            holder.layoutSent.visibility = View.VISIBLE
            holder.layoutReceived.visibility = View.GONE

            holder.tvSentMessage.text = message.text
            holder.tvSentTime.text = message.time
        } else {
            // Show received message, hide sent message
            holder.layoutSent.visibility = View.GONE
            holder.layoutReceived.visibility = View.VISIBLE

            holder.tvReceivedMessage.text = message.text
            holder.tvReceivedTime.text = message.time
        }
    }

    override fun getItemCount() = messages.size
}

