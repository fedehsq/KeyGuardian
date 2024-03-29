package com.example.keyguardian.adapters

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.keyguardian.R
import com.example.keyguardian.activities.EditSecretActivity
import com.example.keyguardian.models.Secret

const val SECRET_EXTRA = "secret_extra"
private const val TAG = "SecretsAdapter"

class SecretsAdapter(private val secrets: MutableList<String>) :
    RecyclerView.Adapter<SecretsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val row: ConstraintLayout = itemView.findViewById(R.id.secret_name_layout)
        val name: TextView = itemView.findViewById(R.id.secret_name)
        val delete: ImageButton = itemView.findViewById(R.id.delete_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.secret_preview_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val secretTitle = secrets[position]
        holder.name.text = secretTitle
        holder.delete.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(holder.itemView.context)
                .setTitle("Are you sure to delete this field?").setPositiveButton("Yes") { _, _ ->
                    // handle delete event
                    val prefs = EncryptedSharedPreferencesUtils.getInstance(holder.itemView.context)
                    prefs.remove(secretTitle)
                    secrets.removeAt(position)
                    notifyItemRemoved(position)
                }.setNegativeButton("No", null)

            val dialog = dialogBuilder.create()
            dialog.show()
        }
        holder.row.setOnClickListener {
            // handle click event
            val prefs = EncryptedSharedPreferencesUtils.getInstance(holder.itemView.context)
            val secret = Secret.fromJson(prefs.getString(secretTitle))
            // Launch the edit secret activity
            val intent = Intent(holder.itemView.context, EditSecretActivity::class.java)
            intent.putExtra(SECRET_EXTRA, secret.toJson())
            // start the activity
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return secrets.size
    }
}
