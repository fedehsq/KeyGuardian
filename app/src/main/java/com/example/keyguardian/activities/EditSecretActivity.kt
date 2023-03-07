package com.example.keyguardian.activities

import EncryptedSharedPreferencesUtils
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.keyguardian.R
import com.example.keyguardian.adapters.SECRET_EXTRA
import com.example.keyguardian.adapters.SecretAdapter
import com.example.keyguardian.databinding.ActivityEditSecretBinding
import com.example.keyguardian.models.KeyValuePair
import com.example.keyguardian.models.Secret
import com.google.android.material.textfield.TextInputEditText


private const val TAG = "EditSecretActivity"

class EditSecretActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditSecretBinding
    private lateinit var prefs: EncryptedSharedPreferencesUtils
    private lateinit var secret: Secret

    private fun addSecretContent() {
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.dialog_edit_content, null)
        val label = dialogView.findViewById<TextInputEditText>(R.id.label_text_input_edit_text)
        val secretContent =
            dialogView.findViewById<TextInputEditText>(R.id.secret_text_input_edit_text)

        val dialogBuilder = AlertDialog.Builder(this).setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                // Save the edited content to the Secret object
                val editedKey = label.text.toString()
                val editedSecret = secretContent.text.toString()
                val keyValuePair = KeyValuePair(editedKey, editedSecret)
                secret.secretContent.add(keyValuePair)
                prefs.putString(secret.name, secret.toJson())
                // Update the value in the UI
                binding.secretContentList.adapter!!.notifyItemInserted(secret.secretContent.size)
            }.setNegativeButton("Cancel", null)

        val dialog = dialogBuilder.create()
        dialog.show()
    }
    private fun createEditTitleDialog(title: String) {
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.dialog_edit_content, null)
        val label = dialogView.findViewById<TextInputEditText>(R.id.label_text_input_edit_text)
        val scr =
            dialogView.findViewById<TextInputEditText>(R.id.secret_text_input_edit_text)
        scr.visibility = View.INVISIBLE
        label.setText(title)
        val dialogBuilder = AlertDialog.Builder(this).setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                // Save the edited content to the Secret object
                val secretName = label.text.toString()
                supportActionBar?.title = secretName
                secret.name = secretName
                prefs.putString(secretName, secret.toJson())

            }.setNegativeButton("Cancel", null)

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_secret)
        binding = ActivityEditSecretBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Read the secret from the intent
        secret = Secret.fromJson( intent.getStringExtra(SECRET_EXTRA)!!)
        supportActionBar?.title = secret.name
        binding.secretContentList.adapter = SecretAdapter(secret)
        binding.secretContentList.layoutManager = LinearLayoutManager(this)
        prefs = EncryptedSharedPreferencesUtils.getInstance(this)

        binding.fab.setOnClickListener {
            addSecretContent()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_edit -> {
            createEditTitleDialog(supportActionBar?.title.toString())
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

}