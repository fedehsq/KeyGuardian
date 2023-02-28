package com.example.keyguardian.activities

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.keyguardian.R
import com.example.keyguardian.adapters.SECRET_CONTENT_EXTRA
import com.example.keyguardian.adapters.SECRET_NAME_EXTRA
import com.example.keyguardian.adapters.SecretContentAdapter
import com.example.keyguardian.databinding.ActivityEditSecretBinding
import com.example.keyguardian.models.Secret


private const val TAG = "EditSecretActivity"

class EditSecretActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditSecretBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_secret)
        binding = ActivityEditSecretBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Read the secret from the intent
        val secretIntent = intent.getStringExtra(SECRET_CONTENT_EXTRA)
        val secret: Secret = Secret.fromJson(secretIntent!!)

        binding.secretTitle.text = intent.getStringExtra(SECRET_NAME_EXTRA)
        binding.secretContentList.adapter = SecretContentAdapter(secret)
        binding.secretContentList.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_edit -> {
            // Change the icon
            item.setIcon(R.drawable.baseline_edit_off_24)
            // Open dialog to edit the secret

            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

}