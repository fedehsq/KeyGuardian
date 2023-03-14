package com.example.keyguardian.activities

import EncryptedSharedPreferencesUtils
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keyguardian.R
import com.example.keyguardian.adapters.SECRET_EXTRA
import com.example.keyguardian.adapters.SecretsAdapter
import com.example.keyguardian.databinding.ActivitySecretsListBinding
import com.example.keyguardian.models.KeyValuePair
import com.example.keyguardian.models.Secret
import com.example.keyguardian.security.MasterKeyUtils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

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
}