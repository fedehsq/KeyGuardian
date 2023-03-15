package com.example.keyguardian.activities

import EncryptedSharedPreferencesUtils
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keyguardian.R
import com.example.keyguardian.adapters.SECRET_EXTRA
import com.example.keyguardian.adapters.SecretsAdapter
import com.example.keyguardian.databinding.ActivitySecretsListBinding
import com.example.keyguardian.models.Secret
import com.google.android.material.textfield.TextInputEditText
import java.util.Base64

private const val TAG = "SecretsListActivity"

class SecretsListActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecretsListBinding
    private lateinit var prefs: EncryptedSharedPreferencesUtils

    private fun addSecretContentDialog() {
        val dialogView = layoutInflater.inflate(R.layout.edit_secret_title, null)
        val title = dialogView.findViewById<TextInputEditText>(R.id.edit_title_text_input_edit_text)
        val dialogBuilder = android.app.AlertDialog.Builder(this).setView(dialogView)
            .setPositiveButton("Ok") { _, _ ->
                val secretName = title.text.toString()
                val secret = Secret(secretName, mutableListOf())
                prefs.putString(secretName, secret.toJson())
                // Launch the EditSecretActivity
                val intent = android.content.Intent(this, EditSecretActivity::class.java)
                intent.putExtra(SECRET_EXTRA, secret.toJson())
                startActivity(intent)
            }.setNegativeButton("Cancel", null)

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun showSecretsDialog() {
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.show_secrets_dialog, null)
        val secretsEditText = dialogView.findViewById<TextInputEditText>(R.id.show_secrets_text_input_edit_text)
        val encodeButton = dialogView.findViewById<android.widget.Button>(R.id.encode_button)
        val secrets = prefs.getAll().toList()
        var stringSecrets = ""
        for (secret in secrets) {
            stringSecrets += secret.first + ": " + secret.second
            secretsEditText.setText(stringSecrets)
        }
        encodeButton.setOnClickListener {
            val encodedSecrets = Base64.getEncoder().encodeToString(stringSecrets.toByteArray())
            secretsEditText.setText(encodedSecrets)
        }
        val dialog = AlertDialog.Builder(this).setView(dialogView).create()
        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivitySecretsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.title = "Secrets"

        prefs = EncryptedSharedPreferencesUtils.getInstance(this)
        val secretsName = prefs.getKeys()
        binding.secretsList.adapter = SecretsAdapter(secretsName.toMutableList())
        binding.secretsList.layoutManager = LinearLayoutManager(this)
        binding.fab.setOnClickListener {
            // Open dialog to create a new secret
            addSecretContentDialog()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_download_secrets, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_download -> {
            showSecretsDialog()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}