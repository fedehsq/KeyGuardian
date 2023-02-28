package com.example.keyguardian.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.keyguardian.R
import com.example.keyguardian.models.Secret


class SecretContentAdapter(private val secret: Secret) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SecretViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val keyTextView: TextView = itemView.findViewById(R.id.content_key)
        private val valueTextView: TextView = itemView.findViewById(R.id.content_value)

        fun bind(key: String, value: String) {
            keyTextView.text = key
            valueTextView.text = value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.secret_content_layout, parent, false)
        return SecretViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SecretViewHolder) {
            val field = secret.content[position]
            holder.bind(field.key, field.value)
        }
    }

    override fun getItemCount(): Int {
        return secret.content.size
    }
}