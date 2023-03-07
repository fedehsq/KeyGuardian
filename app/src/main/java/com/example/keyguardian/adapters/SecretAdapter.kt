package com.example.keyguardian.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.keyguardian.R
import com.example.keyguardian.models.Secret
import com.google.android.material.textfield.TextInputEditText


class SecretAdapter(private val secret: Secret) : RecyclerView.Adapter<SecretAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val keyTextView: TextView = itemView.findViewById(R.id.content_key)
        val valueTextView: TextView = itemView.findViewById(R.id.content_value)
        val editButton: ImageButton = itemView.findViewById(R.id.edit_content)
        val deleteButton: ImageButton = itemView.findViewById(R.id.delete_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.secret_row_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return secret.secretContent.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.keyTextView.text = secret.secretContent[holder.adapterPosition].key
        holder.valueTextView.text = secret.secretContent[holder.adapterPosition].value
        holder.editButton.setOnClickListener {
            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogView = inflater.inflate(R.layout.dialog_edit_content, null)
            val label = dialogView.findViewById<TextInputEditText>(R.id.label_text_input_edit_text)
            val secretContent =
                dialogView.findViewById<TextInputEditText>(R.id.secret_text_input_edit_text)
            label.setText(secret.secretContent[holder.adapterPosition].key)
            secretContent.setText(secret.secretContent[holder.adapterPosition].value)

            val dialogBuilder = AlertDialog.Builder(holder.itemView.context).setView(dialogView)
                .setPositiveButton("Save") { _, _ ->
                    // Save the edited content to the Secret object
                    val editedKey = label.text.toString()
                    val editedSecret = secretContent.text.toString()
                    secret.secretContent[holder.adapterPosition].key = editedKey
                    secret.secretContent[holder.adapterPosition].value = editedSecret
                    val prefs = EncryptedSharedPreferencesUtils.getInstance(holder.itemView.context)
                    prefs.putString(secret.name, secret.toJson())
                    // Update the value in the UI
                    holder.keyTextView.text = editedKey
                    holder.valueTextView.text = editedSecret
                }.setNegativeButton("Cancel", null)

            val dialog = dialogBuilder.create()
            dialog.show()
        }
        holder.deleteButton.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(holder.itemView.context)
                .setTitle("Are you sure to delete this field?").setPositiveButton("Yes") { _, _ ->
                    // Save the edited content to the Secret object
                    secret.secretContent.removeAt(holder.adapterPosition)
                    val prefs = EncryptedSharedPreferencesUtils.getInstance(holder.itemView.context)
                    prefs.putString(secret.name, secret.toJson())
                    notifyItemRemoved(holder.adapterPosition)
                }.setNegativeButton("No", null)

            val dialog = dialogBuilder.create()
            dialog.show()
        }
    }
}