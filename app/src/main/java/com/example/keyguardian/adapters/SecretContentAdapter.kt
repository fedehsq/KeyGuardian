package com.example.keyguardian.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.keyguardian.R
import com.example.keyguardian.models.KeyValuePair
import com.example.keyguardian.models.Secret
import com.google.android.material.textfield.TextInputEditText


class SecretContentAdapter(private val secret: Secret) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class SecretViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val keyTextView: TextView = itemView.findViewById(R.id.content_key)
        private val valueTextView: TextView = itemView.findViewById(R.id.content_value)
        private val editButton: ImageButton = itemView.findViewById(R.id.edit_content)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.edit_content)

        fun bind(content: KeyValuePair) {
            keyTextView.text = content.key
            valueTextView.text = content.value
            editButton.setOnClickListener {
                createEditDialog(content)
            }
            deleteButton.setOnClickListener {
                deleteDialog(content)
            }
        }

        private fun deleteDialog(content: KeyValuePair) {

            val dialogBuilder = AlertDialog.Builder(itemView.context)
                .setTitle("Are you sure to delete this field?")
                .setPositiveButton("Save") { _, _ ->
                    // Save the edited content to the Secret object
                    val editedKey = label.text.toString()
                    val editedSecret = secret.text.toString()
                    content.key = editedKey
                    content.value = editedSecret
                    // Update the value in the UI
                    keyTextView.text = editedKey
                    valueTextView.text = editedSecret
                }
                .setNegativeButton("Cancel", null)

            val dialog = dialogBuilder.create()
            dialog.show()
        }


        private fun createEditDialog(content: KeyValuePair) {
            val inflater = LayoutInflater.from(itemView.context)
            val dialogView = inflater.inflate(R.layout.dialog_edit_content, null)
            val label =
                dialogView.findViewById<TextInputEditText>(R.id.label_text_input_edit_text)
            val secret =
                dialogView.findViewById<TextInputEditText>(R.id.secret_text_input_edit_text)
            label.setText(content.key)
            secret.setText(content.value)

            val dialogBuilder = AlertDialog.Builder(itemView.context)
                .setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    // Save the edited content to the Secret object
                    val editedKey = label.text.toString()
                    val editedSecret = secret.text.toString()
                    content.key = editedKey
                    content.value = editedSecret
                    // Update the value in the UI
                    keyTextView.text = editedKey
                    valueTextView.text = editedSecret
                }
                .setNegativeButton("Cancel", null)

            val dialog = dialogBuilder.create()
            dialog.show()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.secret_content_layout, parent, false)
        return SecretViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SecretViewHolder) {
            holder.bind(secret.secretContent[position])
        }
    }

    override fun getItemCount(): Int {
        return secret.secretContent.size
    }
}